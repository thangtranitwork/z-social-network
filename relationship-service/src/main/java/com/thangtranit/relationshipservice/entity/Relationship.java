package com.thangtranit.relationshipservice.entity;

import com.thangtranit.relationshipservice.enums.RelationshipStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "relationships")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Relationship {
    @Id
    String id;
    String userId1;
    String userId2;
    String sender;
    RelationshipStatus status;
    @Builder.Default
    LocalDateTime sentTime = LocalDateTime.now();
    LocalDateTime acceptedTime;
}
