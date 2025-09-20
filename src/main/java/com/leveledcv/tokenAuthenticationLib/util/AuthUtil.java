package com.leveledcv.tokenAuthenticationLib.util;

import com.leveledcv.tokenAuthenticationLib.context.AuthenticationContext;
import com.leveledcv.tokenAuthenticationLib.models.TokenVerificationResponse;
import com.leveledcv.tokenAuthenticationLib.models.User;
import com.leveledcv.tokenAuthenticationLib.models.enums.UserStatus;

/**
 * Utility class to easily access authenticated user information in controllers
 */
public class AuthUtil {

    /**
     * Get the current authenticated user as a User object
     * @return User object with username, email, and status, or null if not authenticated
     */
    public static User getUser() {
        TokenVerificationResponse currentUser = AuthenticationContext.getCurrentUser();
        if (currentUser == null) {
            return null;
        }

        return new User(currentUser.getUsername(), currentUser.getEmail(), currentUser.getStatus());
    }

    /**
     * Get the complete user information for the current authenticated user
     * @return TokenVerificationResponse containing user details, or null if not authenticated
     */
    public static TokenVerificationResponse getCurrentUser() {
        return AuthenticationContext.getCurrentUser();
    }

    /**
     * Get the username of the current authenticated user
     * @return username string, or null if not authenticated
     */
    public static String getUsername() {
        return AuthenticationContext.getCurrentUsername();
    }

    /**
     * Get the email of the current authenticated user
     * @return email string, or null if not authenticated
     */
    public static String getEmail() {
        return AuthenticationContext.getCurrentUserEmail();
    }

    /**
     * Get the status of the current authenticated user
     * @return UserStatus enum, or null if not authenticated
     */
    public static UserStatus getStatus() {
        return AuthenticationContext.getCurrentUserStatus();
    }

    /**
     * Check if there is a currently authenticated user
     * @return true if user is authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        return getCurrentUser() != null;
    }
}
