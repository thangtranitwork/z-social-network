package com.thangtranit.mailservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse <T>{
    @Builder.Default
    int code = 200;
    @Builder.Default
    String message = "success";
    @Builder.Default
    String timestamp = LocalDateTime.now().toString();
    T body;
}