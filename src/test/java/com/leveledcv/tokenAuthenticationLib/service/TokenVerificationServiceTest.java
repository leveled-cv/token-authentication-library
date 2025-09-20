package com.leveledcv.tokenAuthenticationLib.service;

import com.leveledcv.tokenAuthenticationLib.config.AuthProperties;
import com.leveledcv.tokenAuthenticationLib.models.TokenVerificationResponse;
import com.leveledcv.tokenAuthenticationLib.models.enums.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenVerificationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AuthProperties authProperties;

    private TokenVerificationService tokenVerificationService;

    @BeforeEach
    void setUp() {
        when(authProperties.getTokenVerifyUrl()).thenReturn("http://localhost:8081/token/verify");
        tokenVerificationService = new TokenVerificationService(authProperties, restTemplate);
    }

    @Test
    void testVerifyToken_Success() {
        // Arrange
        String token = "Bearer valid-token";
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("username", "testuser");
        responseMap.put("email", "test@example.com");
        responseMap.put("status", "ACTIVE");

        ResponseEntity<Object> response = new ResponseEntity<>(responseMap, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), any(), any(), eq(Object.class))).thenReturn(response);

        // Act
        TokenVerificationResponse result = tokenVerificationService.verifyToken(token);

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals(UserStatus.ACTIVE, result.getStatus());
    }

    @Test
    void testVerifyToken_SessionNotFound() {
        // Arrange
        String token = "Bearer invalid-token";
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("error", "Session not found");

        ResponseEntity<Object> response = new ResponseEntity<>(responseMap, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), any(), any(), eq(Object.class))).thenReturn(response);

        // Act
        TokenVerificationResponse result = tokenVerificationService.verifyToken(token);

        // Assert
        assertNull(result);
    }

    @Test
    void testVerifyToken_Exception() {
        // Arrange
        String token = "Bearer error-token";
        when(restTemplate.exchange(anyString(), any(), any(), eq(Object.class)))
                .thenThrow(new RuntimeException("Network error"));

        // Act
        TokenVerificationResponse result = tokenVerificationService.verifyToken(token);

        // Assert
        assertNull(result);
    }
}
