package com.leveledcv.tokenAuthenticationLib.models;

import com.leveledcv.tokenAuthenticationLib.models.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response model for token verification containing user information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenVerificationResponse {
    private String username;
    private String email;
    private UserStatus status;
}
