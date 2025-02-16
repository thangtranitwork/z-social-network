package com.thangtranit.relationshipservice.dto.response;

import java.time.LocalDateTime;

public record AddFriendInvitationResponse(
        String id,
        LocalDateTime sentTime
) {
}
