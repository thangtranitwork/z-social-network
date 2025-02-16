package com.thangtranit.profileservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thangtranit.profileservice.exception.AppException;
import com.thangtranit.profileservice.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonConverter {
    private final ObjectMapper objectMapper;

    public LinkedHashMap convertJsonToMap(String json) {
        try {
            return objectMapper.readValue(json, LinkedHashMap.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED_ERROR);
        }
    }
}
