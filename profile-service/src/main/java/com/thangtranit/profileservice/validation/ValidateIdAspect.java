package com.thangtranit.profileservice.validation;

import com.thangtranit.profileservice.exception.AppException;
import com.thangtranit.profileservice.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class ValidateIdAspect {

    @Before("@annotation(ValidateId)")
    public void validateId(JoinPoint joinPoint) {
        // Lấy RequestAttributes
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();

            String uid = request.getHeader("uid");

            if (!StringUtils.hasText(uid)) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
        }
    }
}

