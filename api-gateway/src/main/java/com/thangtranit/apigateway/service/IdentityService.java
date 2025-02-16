package com.thangtranit.apigateway.service;

import com.thangtranit.apigateway.client.IdentityClient;
import com.thangtranit.apigateway.dto.response.ApiResponse;
import com.thangtranit.apigateway.dto.response.IntrospectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class IdentityService {
    private final IdentityClient identityClient;

    public Mono<ApiResponse<IntrospectResponse>> introspect(String token) {
        return identityClient.introspect(token);
    }
}
