package com.leveledcv.tokenAuthenticationLib.context;

import com.leveledcv.tokenAuthenticationLib.models.TokenVerificationResponse;

/**
 * Thread-local context to store authentication information for the current request
 */
public class AuthenticationContext {

    private static final ThreadLocal<TokenVerificationResponse> userContext = new ThreadLocal<>();

    /**
     * Set the current user information
     */
    public static void setCurrentUser(TokenVerificationResponse user) {
        userContext.set(user);
    }

    /**
     * Get the current user information
     */
    public static TokenVerificationResponse getCurrentUser() {
        return userContext.get();
    }

    /**
     * Get the current username
     */
    public static String getCurrentUsername() {
        TokenVerificationResponse user = getCurrentUser();
        return user != null ? user.getUsername() : null;
    }

    /**
     * Get the current user email
     */
    public static String getCurrentUserEmail() {
        TokenVerificationResponse user = getCurrentUser();
        return user != null ? user.getEmail() : null;
    }

    /**
     * Get the current user status
     */
    public static com.leveledcv.tokenAuthenticationLib.models.enums.UserStatus getCurrentUserStatus() {
        TokenVerificationResponse user = getCurrentUser();
        return user != null ? user.getStatus() : null;
    }

    /**
     * Clear the current user context
     */
    public static void clear() {
        userContext.remove();
    }
}
