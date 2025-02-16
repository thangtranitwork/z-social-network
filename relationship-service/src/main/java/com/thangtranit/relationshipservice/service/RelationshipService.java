package com.thangtranit.relationshipservice.service;

import com.thangtranit.relationshipservice.dto.response.AddFriendInvitationResponse;
import com.thangtranit.relationshipservice.entity.Relationship;
import com.thangtranit.relationshipservice.enums.RelationshipStatus;
import com.thangtranit.relationshipservice.exception.AppException;
import com.thangtranit.relationshipservice.exception.ErrorCode;
import com.thangtranit.relationshipservice.repository.RelationshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RelationshipService {
    private final RelationshipRepository relationshipRepository;

    public Boolean isFriend(String uid, String id) {
        Optional<Relationship> relationship = relationshipRepository.findRelationship(uid, id);
        if (relationship.isPresent() && RelationshipStatus.FRIEND.equals(relationship.get().getStatus())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Slice<String> getAllFriends(String uid, String targetId, Pageable pageable) {
        if (!uid.equals(targetId)) {
            relationshipRepository.findRelationship(uid, targetId).ifPresent(
                    relationship -> {
                        if (RelationshipStatus.BLOCKED.equals(relationship.getStatus())) {
                            throw new AppException(ErrorCode.HAVE_ALREADY_BLOCKED);
                        }
                    });
        }
        return relationshipRepository.findFriendIdsByUserId(targetId, pageable);
    }

    public List<AddFriendInvitationResponse> getAllSentAddFriendInvitations(String uid) {
        return relationshipRepository.findSentFriendRequests(uid).stream()
                .map(relationship -> new AddFriendInvitationResponse(relationship.getSender(), relationship.getSentTime()))
                .toList();
    }

    public List<AddFriendInvitationResponse> getAllReceivedAddFriendInvitations(String uid) {
        return relationshipRepository.findReceivedFriendRequests(uid).stream()
                .map(relationship -> {
                    String friendId = (relationship.getUserId1().equals(uid) ? relationship.getUserId2() : relationship.getUserId1());
                    return new AddFriendInvitationResponse(friendId, relationship.getSentTime());
                })
                .toList();
    }

    public Set<String> getBlockedUsers(String uid) {
        return relationshipRepository.findBlockedUsers(uid);
    }

    public void addFriend(String uid, String targetId) {
        relationshipRepository.findRelationship(uid, targetId).ifPresentOrElse(
                relationship -> {
                    if (RelationshipStatus.PENDING_ADD_FRIEND_INVITATION.equals(relationship.getStatus()) && relationship.getSender().equals(targetId)) {
                        relationship.setStatus(RelationshipStatus.FRIEND);
                        relationship.setAcceptedTime(LocalDateTime.now());
                        relationshipRepository.save(relationship);
                    } else if (RelationshipStatus.PENDING_ADD_FRIEND_INVITATION.equals(relationship.getStatus()) && relationship.getSender().equals(uid)) {
                        throw new AppException(ErrorCode.HAVE_ALREADY_SENT_ADD_FRIEND_INVITATION);
                    } else if (RelationshipStatus.FRIEND.equals(relationship.getStatus())) {
                        throw new AppException(ErrorCode.HAVE_ALREADY_BE_FRIEND);
                    } else if (RelationshipStatus.BLOCKED.equals(relationship.getStatus())) {
                        throw new AppException(ErrorCode.HAVE_ALREADY_BLOCKED);
                    }
                },
                () -> {
                    Pair<String, String> pair = sortId(uid, targetId);
                    Relationship relationship = Relationship.builder()
                            .userId1(pair.getFirst())
                            .userId2(pair.getSecond())
                            .sender(uid)
                            .status(RelationshipStatus.PENDING_ADD_FRIEND_INVITATION)
                            .build();
                    relationshipRepository.save(relationship);
                });
    }

    public void unfriend(String uid, String targetId) {
        relationshipRepository.findRelationship(uid, targetId).ifPresentOrElse(
                relationship -> {
                    if (RelationshipStatus.FRIEND.equals(relationship.getStatus())) {
                        relationshipRepository.delete(relationship);
                    } else {
                        throw new AppException(ErrorCode.HAVE_NOT_ALREADY_BE_FRIEND);
                    }
                },
                () -> {
                    throw new AppException(ErrorCode.RELATIONSHIP_NOT_FOUND);
                }
        );
    }

    public void acceptFriendInvitation(String uid, String targetId) {
        relationshipRepository.findRelationship(uid, targetId).ifPresentOrElse(
                relationship -> {
                    if (RelationshipStatus.PENDING_ADD_FRIEND_INVITATION.equals(relationship.getStatus()) && relationship.getSender().equals(targetId)) {
                        relationship.setStatus(RelationshipStatus.FRIEND);
                        relationship.setAcceptedTime(LocalDateTime.now());
                        relationshipRepository.save(relationship);
                    } else if (RelationshipStatus.BLOCKED.equals(relationship.getStatus())) {
                        throw new AppException(ErrorCode.HAVE_ALREADY_BLOCKED);
                    } else {
                        throw new AppException(ErrorCode.ACCEPT_ADD_FRIEND_INVITATION_FAILED);
                    }
                },
                () -> {
                    throw new AppException(ErrorCode.ADD_FRIEND_INVITATION_NOT_FOUND);
                }
        );
    }

    public void rejectFriendInvitation(String uid, String targetId) {
        relationshipRepository.findRelationship(uid, targetId).ifPresentOrElse(
                relationship -> {
                    if (RelationshipStatus.PENDING_ADD_FRIEND_INVITATION.equals(relationship.getStatus()) && relationship.getSender().equals(targetId)) {
                        relationshipRepository.delete(relationship);
                    } else {
                        throw new AppException(ErrorCode.REJECT_ADD_FRIEND_INVITATION_FAILED);
                    }
                },
                () -> {
                    throw new AppException(ErrorCode.ADD_FRIEND_INVITATION_NOT_FOUND);
                }
        );
    }

    public void cancelFriendInvitation(String uid, String targetId) {
        relationshipRepository.findRelationship(uid, targetId).ifPresentOrElse(
                relationship -> {
                    if (RelationshipStatus.PENDING_ADD_FRIEND_INVITATION.equals(relationship.getStatus()) && relationship.getSender().equals(uid)) {
                        relationshipRepository.delete(relationship);
                    } else {
                        throw new AppException(ErrorCode.CANCEL_ADD_FRIEND_INVITATION_FAILED);
                    }
                },
                () -> {
                    throw new AppException(ErrorCode.ADD_FRIEND_INVITATION_NOT_FOUND);
                }
        );
    }

    public void blockUser(String uid, String targetId) {
        relationshipRepository.findRelationship(targetId, uid).ifPresentOrElse(
                relationship -> {
                    if (!RelationshipStatus.BLOCKED.equals(relationship.getStatus())) {
                        relationship.setStatus(RelationshipStatus.BLOCKED);
                        relationship.setSender(uid);
                        relationshipRepository.save(relationship);
                    } else {
                        throw new AppException(ErrorCode.HAVE_ALREADY_BLOCKED);
                    }
                },
                () -> {
                    Pair<String, String> pair = sortId(targetId, uid);
                    Relationship relationship = Relationship.builder()
                            .userId1(pair.getFirst())
                            .userId2(pair.getSecond())
                            .status(RelationshipStatus.BLOCKED)
                            .sender(uid)
                            .build();
                    relationshipRepository.save(relationship);
                }
        );
    }

    public void unblockUser(String uid, String targetId) {
        relationshipRepository.findRelationship(targetId, uid).ifPresentOrElse(
                relationship -> {
                    if (relationship.getStatus() == RelationshipStatus.BLOCKED) {
                        relationshipRepository.delete(relationship);
                    } else {
                        throw new AppException(ErrorCode.HAVE_NOT_BLOCKED);
                    }
                },
                () -> {
                    throw new AppException(ErrorCode.RELATIONSHIP_NOT_FOUND);
                }
        );
    }

    public Pair<String, String> sortId(String id1, String id2) {
        if (id1.compareTo(id2) < 0) {
            return Pair.of(id1, id2);
        } else {
            return Pair.of(id2, id1);
        }
    }

    public Boolean isBlocked(String uid, String targetId) {
        Optional<Relationship> relationship = relationshipRepository.findRelationship(uid, targetId);
        return relationship.isPresent() && RelationshipStatus.BLOCKED.equals(relationship.get().getStatus());
    }
}
