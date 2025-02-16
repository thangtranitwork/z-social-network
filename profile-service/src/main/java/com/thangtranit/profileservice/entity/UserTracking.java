package com.thangtranit.profileservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document(collection = "view-profile-tracking")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserTracking {
    @Id
    String id;
    Map<String, Integer> users = new HashMap<>();

    public void view(String id) {
        users.put(id, users.getOrDefault(id, 0) + 1);
    }
}
