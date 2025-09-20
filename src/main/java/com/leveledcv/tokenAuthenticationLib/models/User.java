package com.leveledcv.tokenAuthenticationLib.models;

import com.leveledcv.tokenAuthenticationLib.models.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User model for easy access to authenticated user information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    public String username;
    public String email;
    public UserStatus status;

    /**
     * Check if the user is active
     * @return true if user status is ACTIVE
     */
    public boolean isActive() {
        return status == UserStatus.ACTIVE;
    }

    /**
     * Check if the user can log in
     * @return true if user can log in
     */
    public boolean canLogin() {
        return status != null && status.canLogin();
    }

    /**
     * Check if the account is locked
     * @return true if account is locked
     */
    public boolean isLocked() {
        return status != null && status.isLocked();
    }
}
