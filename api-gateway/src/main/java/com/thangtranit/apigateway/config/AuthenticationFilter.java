package com.thangtranit.apigateway.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thangtranit.apigateway.dto.response.ApiResponse;
import com.thangtranit.apigateway.service.IdentityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {
    private final IdentityService identityService;
    private final ObjectMapper objectMapper;

    private static final Set<String> PUBLIC_GET_ENDPOINTS = Set.of(
            "/identity/.*"
    );

    private static final Set<String> PUBLIC_POST_ENDPOINTS = Set.of(
            "/identity/.*"
    );

    private static final Set<String> FLEX_ENDPOINTS = Set.of(
            "/profile/public/.*",
            "/file/get/.*",
            "/file/info/.*"
    );
    // PATCH & DELETE must authenticate

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        HttpMethod method = exchange.getRequest().getMethod();
        List<String> auth = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);

        // Nếu là public endpoint, bỏ qua xác thực
        if (isPublicEndpoint(path, method)) {
            return chain.filter(exchange);
        }

        if(isFlexEndpoint(path) && auth == null) {
            return chain.filter(exchange);
        }

        if (auth != null && auth.get(0).startsWith("Bearer ")) {
            String token = auth.get(0).replace("Bearer ", "");
            return authenticate(exchange, chain, token);
        }

        return unauthenticated(exchange.getResponse());
    }

    private Mono<Void> authenticate(ServerWebExchange exchange, GatewayFilterChain chain, String token) {
        return identityService.introspect(token)
                .flatMap(result -> {
                    if (result.getBody().valid()) {
                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                .header("uid", result.getBody().id())
                                .header("role", result.getBody().role())
                                .build();
                        ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

                        return chain.filter(mutatedExchange);
                    }
                    return unauthenticated(exchange.getResponse());
                })
                .onErrorResume(e -> {
                    log.error("Error during introspection: {}", e.getMessage());
                    return unauthenticated(exchange.getResponse());
                });
    }


    @Override
    public int getOrder() {
        return -1;
    }

    private boolean isFlexEndpoint(String path) {
        return FLEX_ENDPOINTS.stream().anyMatch(path::matches);
    }

    private boolean isPublicEndpoint(String path, HttpMethod method) {
        if (HttpMethod.GET.equals(method)) {
            return PUBLIC_GET_ENDPOINTS.stream().anyMatch(path::matches);
        }

        if (HttpMethod.POST.equals(method)) {
            return PUBLIC_POST_ENDPOINTS.stream().anyMatch(path::matches);
        }

        return false;
    }

    private Mono<Void> unauthenticated(ServerHttpResponse response) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(1998)
                .message("Unauthenticated")
                .build();

        String body = "";
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
}
