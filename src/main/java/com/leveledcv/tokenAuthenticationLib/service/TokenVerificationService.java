package com.leveledcv.tokenAuthenticationLib.service;

import com.leveledcv.tokenAuthenticationLib.config.AuthProperties;
import com.leveledcv.tokenAuthenticationLib.models.TokenVerificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Service responsible for verifying authentication tokens
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TokenVerificationService {

    private final AuthProperties authProperties;
    private final RestTemplate restTemplate;

    /**
     * Verifies the provided token by making a call to the configured verification service
     *
     * @param token The authentication token to verify
     * @return TokenVerificationResponse if token is valid, null if invalid
     */
    public TokenVerificationResponse verifyToken(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Object> response = restTemplate.exchange(
                authProperties.getTokenVerifyUrl(),
                HttpMethod.GET,
                entity,
                Object.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Object responseBody = response.getBody();

                // Check if response contains error
                if (responseBody instanceof Map<?, ?> responseMap) {
                    if (responseMap.containsKey("error") &&
                        "Session not found".equals(responseMap.get("error"))) {
                        log.debug("Token verification failed: Session not found");
                        return null;
                    }

                    // Parse successful response
                    try {
                        return parseTokenResponse(responseMap);
                    } catch (Exception e) {
                        log.error("Failed to parse token verification response", e);
                        return null;
                    }
                }
            }

            log.debug("Token verification failed with status: {}", response.getStatusCode());
            return null;

        } catch (Exception e) {
            log.error("Error during token verification", e);
            return null;
        }
    }

    private TokenVerificationResponse parseTokenResponse(Map<?, ?> responseMap) {
        TokenVerificationResponse response = new TokenVerificationResponse();

        if (responseMap.get("username") != null) {
            response.setUsername(responseMap.get("username").toString());
        }

        if (responseMap.get("email") != null) {
            response.setEmail(responseMap.get("email").toString());
        }

        if (responseMap.get("status") != null) {
            try {
                String statusStr = responseMap.get("status").toString();
                response.setStatus(com.leveledcv.tokenAuthenticationLib.models.enums.UserStatus.valueOf(statusStr));
            } catch (IllegalArgumentException e) {
                log.warn("Unknown user status: {}", responseMap.get("status"));
            }
        }

        return response;
    }
}
