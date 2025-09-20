package com.leveledcv.tokenAuthenticationLib.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for the authentication library
 */
@Data
@Component
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {

    /**
     * URL for token verification service
     * Default: http://localhost:8081/token/verify
     */
    private String tokenVerifyUrl = "http://localhost:8081/token/verify";

    /**
     * Header name to extract the token from
     * Default: Authorization
     */
    private String tokenHeader = "Authorization";

    /**
     * Token prefix (e.g., "Bearer ")
     * Default: Bearer
     */
    private String tokenPrefix = "Bearer ";
}
