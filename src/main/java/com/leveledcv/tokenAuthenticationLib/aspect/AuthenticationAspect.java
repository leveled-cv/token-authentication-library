package com.leveledcv.tokenAuthenticationLib.aspect;

import com.leveledcv.tokenAuthenticationLib.annotations.RequireAuth;
import com.leveledcv.tokenAuthenticationLib.config.AuthProperties;
import com.leveledcv.tokenAuthenticationLib.context.AuthenticationContext;
import com.leveledcv.tokenAuthenticationLib.models.TokenVerificationResponse;
import com.leveledcv.tokenAuthenticationLib.service.TokenVerificationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

/**
 * Aspect that handles authentication for methods annotated with @RequireAuth
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationAspect {

    private final TokenVerificationService tokenVerificationService;
    private final AuthProperties authProperties;

    /**
     * Intercepts methods annotated with @RequireAuth or classes annotated with @RequireAuth
     */
    @Around("@annotation(requireAuth) || @within(requireAuth)")
    public Object authenticate(ProceedingJoinPoint joinPoint, RequireAuth requireAuth) throws Throwable {
        try {
            // Get the current HTTP request
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            // Extract token from header
            String authHeader = request.getHeader(authProperties.getTokenHeader());
            if (authHeader == null || authHeader.isEmpty()) {
                log.debug("No authorization header found");
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Authorization header is missing");
            }

            // Remove token prefix if present
            String token = authHeader;
            if (authHeader.startsWith(authProperties.getTokenPrefix())) {
                token = authHeader.substring(authProperties.getTokenPrefix().length()).trim();
                // Add the prefix back for the verification call
                token = authProperties.getTokenPrefix() + token;
            }

            if (token.isEmpty()) {
                log.debug("Empty token found");
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid authorization token");
            }

            // Verify token
            TokenVerificationResponse userInfo = tokenVerificationService.verifyToken(token);
            if (userInfo == null) {
                log.debug("Token verification failed");
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid or expired token");
            }

            // Set user context for the current request
            AuthenticationContext.setCurrentUser(userInfo);

            log.debug("Authentication successful for user: {}", userInfo.getUsername());

            // Proceed with the original method
            return joinPoint.proceed();

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Authentication error", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Authentication service error");
        } finally {
            // Clear the context after request processing
            AuthenticationContext.clear();
        }
    }
}
