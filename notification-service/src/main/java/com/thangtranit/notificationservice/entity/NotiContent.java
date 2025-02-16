package com.thangtranit.notificationservice.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "contents")
@Builder
public class NotiContent {
    @Id
    String id;
    String text;
    LocalDateTime time;
    NotiStatus status;
}
