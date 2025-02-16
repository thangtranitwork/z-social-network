package com.thangtranit.relationshipservice.repository;

import com.thangtranit.relationshipservice.entity.Relationship;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RelationshipRepository extends MongoRepository<Relationship, String> {
    @Query("{ $or: [ { 'userId1': ?0, 'userId2': ?1 }, { 'userId1': ?1, 'userId2': ?0 } ] }")
    Optional<Relationship> findRelationship(String userId1, String userId2);

    @Aggregation(pipeline = {
            "{ $match: { 'status': 'FRIEND', $or: [{'userId1': ?0}, {'userId2': ?0}] } }",
            "{ $project: { " +
                    "    friendId: { " +
                    "        $cond: { " +
                    "            if: { $eq: ['$userId1', ?0] }, " +
                    "            then: '$userId2', " +
                    "            else: '$userId1' " +
                    "        } " +
                    "    } " +
                    "} }"
    })
    Slice<String> findFriendIdsByUserId(String userId, Pageable pageable);

    @Aggregation(pipeline = {
            "{ $match: { 'status': 'BLOCKED', $or: [{'userId1': ?0}, {'userId2': ?0}] } }",
            "{ $project: { " +
                    "    friendId: { " +
                    "        $cond: { " +
                    "            if: { $eq: ['$userId1', ?0] }, " +
                    "            then: '$userId2', " +
                    "            else: '$userId1' " +
                    "        } " +
                    "    } " +
                    "} }"
    })
    Set<String> findBlockedUsers(String userId);

    @Query(value = "{ 'sender': ?0, 'status': 'PENDING_ADD_FRIEND_INVITATION' }")
    List<Relationship> findSentFriendRequests(String userId);

    // Lấy danh sách lời mời kết bạn đã nhận
    @Query(value = "{ " +
            "'status': 'PENDING_ADD_FRIEND_INVITATION', " +
            "$or: [" +
            "  { 'userId1': ?0, 'sender': { $ne: ?0 } }, " +
            "  { 'userId2': ?0, 'sender': { $ne: ?0 } }" +
            "] }")
    List<Relationship> findReceivedFriendRequests(String userId);
}
