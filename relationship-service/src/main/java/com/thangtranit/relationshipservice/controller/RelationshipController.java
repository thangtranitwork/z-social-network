package com.thangtranit.relationshipservice.controller;

import com.thangtranit.relationshipservice.dto.response.AddFriendInvitationResponse;
import com.thangtranit.relationshipservice.dto.response.ApiResponse;
import com.thangtranit.relationshipservice.service.RelationshipService;
import com.thangtranit.relationshipservice.validation.ValidateId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class RelationshipController {
    private final RelationshipService relationshipService;

    @GetMapping("/is_friend/{id}")
    @ValidateId
    public ApiResponse<Boolean> isFriend(
            @PathVariable("id") String targetId,
            @RequestHeader("uid") String uid

    ) {
        return ApiResponse.<Boolean>builder()
                .body(relationshipService.isFriend(uid, targetId))
                .build();
    }

    @GetMapping("/friends/{id}")
    public ApiResponse<Slice<String>> getFriends(
            @PathVariable("id") String targetId,
            Pageable pageable,
            @RequestHeader("uid") String uid

    ) {
        return ApiResponse.<Slice<String>>builder()
                .body(relationshipService.getAllFriends(uid, targetId, pageable))
                .build();
    }

    @GetMapping("/sent")
    public ApiResponse<List<AddFriendInvitationResponse>> getSent(
            @RequestHeader("uid") String uid
    ) {
        return ApiResponse.<List<AddFriendInvitationResponse>>builder()
                .body(relationshipService.getAllSentAddFriendInvitations(uid))
                .build();
    }

    @GetMapping("/received")
    public ApiResponse<List<AddFriendInvitationResponse>> getReceived(
            @RequestHeader("uid") String uid
    ) {
        return ApiResponse.<List<AddFriendInvitationResponse>>builder()
                .body(relationshipService.getAllReceivedAddFriendInvitations(uid))
                .build();
    }

    @GetMapping("/blocked_users")
    public ApiResponse<Set<String>> getBlockedUsers(
            @RequestHeader("uid") String uid
    ) {
        return ApiResponse.<Set<String>>builder()
                .body(relationshipService.getBlockedUsers(uid))
                .build();
    }

    @GetMapping("/is_blocked/{id}")
    public ApiResponse<Boolean> isBlocked(
            @PathVariable("id") String targetId,
            @RequestHeader("uid") String uid

    ) {
        return ApiResponse.<Boolean>builder()
                .body(relationshipService.isBlocked(uid, targetId))
                .build();
    }

    @PatchMapping("/accept/{id}")
    @ValidateId
    public ApiResponse<Void> accept(
            @PathVariable("id") String targetId,
            @RequestHeader("uid") String uid
    ) {
        relationshipService.acceptFriendInvitation(uid, targetId);
        return new ApiResponse<>();
    }

    @PutMapping("/block/{id}")
    @ValidateId
    public ApiResponse<Void> block(
            @PathVariable("id") String targetId,
            @RequestHeader("uid") String uid
    ) {
        relationshipService.blockUser(uid, targetId);
        return new ApiResponse<>();
    }

    @PutMapping("/add/{id}")
    @ValidateId
    public ApiResponse<Void> add(
            @PathVariable("id") String targetId,
            @RequestHeader("uid") String uid
    ) {
        relationshipService.addFriend(uid, targetId);
        return new ApiResponse<>();
    }

    @DeleteMapping("/reject/{id}")
    @ValidateId
    public ApiResponse<Void> reject(
            @PathVariable("id") String targetId,
            @RequestHeader("uid") String uid
    ) {
        relationshipService.rejectFriendInvitation(uid, targetId);
        return new ApiResponse<>();
    }

    @DeleteMapping("/cancel/{id}")
    @ValidateId
    public ApiResponse<Void> cancel(
            @PathVariable("id") String targetId,
            @RequestHeader("uid") String uid
    ) {
        relationshipService.cancelFriendInvitation(uid, targetId);
        return new ApiResponse<>();
    }

    @DeleteMapping("/unblock/{id}")
    @ValidateId
    public ApiResponse<Void> unblock(
            @PathVariable("id") String targetId,
            @RequestHeader("uid") String uid
    ) {
        relationshipService.unblockUser(uid, targetId);
        return new ApiResponse<>();
    }

    @DeleteMapping("/unfriend/{id}")
    @ValidateId
    public ApiResponse<Void> unfriend(
            @PathVariable("id") String targetId,
            @RequestHeader("uid") String uid
    ) {
        relationshipService.unfriend(uid, targetId);
        return new ApiResponse<>();
    }
}
