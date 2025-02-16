package com.thangtranit.apigateway.client;

import com.thangtranit.apigateway.dto.response.ApiResponse;
import com.thangtranit.apigateway.dto.response.IntrospectResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface IdentityClient {
    @PostExchange(url = "auth/introspect/{token}")
    Mono<ApiResponse<IntrospectResponse>> introspect(@PathVariable String token);
}
