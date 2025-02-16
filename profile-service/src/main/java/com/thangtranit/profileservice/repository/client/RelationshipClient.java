package com.thangtranit.profileservice.repository.client;

import com.thangtranit.profileservice.config.IdRequestInterceptor;
import com.thangtranit.profileservice.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "relationship-client",
        url = "${client.relationship}",
        configuration = {
                IdRequestInterceptor.class
        })
public interface RelationshipClient {
    @GetMapping(path = "/is_blocked/{id}")
    ApiResponse<Boolean> isBlocked(@PathVariable String id);
}

