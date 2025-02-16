package com.thangtranit.profileservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class IdRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            String uid = requestAttributes
                    .getRequest()
                    .getHeader("uid");
            if (StringUtils.hasText(uid)) {
                requestTemplate.header("uid", uid);
            }
        }
    }
}
