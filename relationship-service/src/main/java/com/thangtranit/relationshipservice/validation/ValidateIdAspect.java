package com.thangtranit.relationshipservice.validation;

import com.thangtranit.relationshipservice.exception.AppException;
import com.thangtranit.relationshipservice.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
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
    public void validateId() {
        // Lấy RequestAttributes
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();

            String uid = request.getHeader("uid");
            String targetId = extractTargetIdFromPath(request.getRequestURI());

            if (!StringUtils.hasText(uid)) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            if (targetId.equals(uid)) {
                throw new AppException(ErrorCode.CAN_NOT_MAKE_A_RELATIONSHIP_WITH_YOURSELF);
            }
        }
    }

    private String extractTargetIdFromPath(String path) {
        String[] parts = path.split("/");
        return parts[parts.length - 1];
    }
}

