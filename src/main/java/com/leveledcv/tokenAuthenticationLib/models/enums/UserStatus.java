package com.leveledcv.tokenAuthenticationLib.models.enums;

import lombok.Getter;

/**
 * Enumeration representing the various states of a user account in the authentication system.
 * This enum provides a clear, standardized way to represent user account status across the application.
 */
@Getter
public enum UserStatus {

    /**
     * User has registered but has not yet verified their email address.
     * User cannot log in until email verification is completed.
     */
    PENDING_VERIFICATION("Pending Email Verification", "Please check your email to verify your account."),

    /**
     * User account is fully active and verified.
     * User can log in and access all features.
     */
    ACTIVE("Active", "Account is active and fully functional."),

    /**
     * Account has been locked due to security concerns or policy violations.
     * User cannot log in until an administrator unlocks the account.
     */
    LOCKED("Locked", "Account has been locked. Please contact support for assistance."),

    /**
     * Account is temporarily locked due to too many failed login attempts.
     * Will be automatically unlocked after a specified time period.
     */
    TEMPORARILY_LOCKED("Temporarily Locked", "Account is temporarily locked due to failed login attempts. Try again later."),

    /**
     * Account has been suspended by an administrator.
     * User cannot log in until suspension is lifted.
     */
    SUSPENDED("Suspended", "Account has been suspended. Please contact support."),

    /**
     * Account has been deactivated by the user or system.
     * User cannot log in but account data is preserved.
     */
    INACTIVE("Inactive", "Account is inactive. Please contact support to reactivate."),

    /**
     * Account is in the process of being deleted.
     * User cannot log in and account will be permanently removed.
     */
    PENDING_DELETION("Pending Deletion", "Account is scheduled for deletion."),

    /**
     * Email verification has expired and needs to be resent.
     * User must request a new verification email.
     */
    VERIFICATION_EXPIRED("Verification Expired", "Email verification has expired. Please request a new verification email."),

    /**
     * Account requires additional verification (e.g., phone, identity documents).
     * User has limited access until additional verification is completed.
     */
    REQUIRES_ADDITIONAL_VERIFICATION("Requires Additional Verification", "Additional verification required to fully activate account.");

    private final String displayName;
    private final String description;

    UserStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Checks if the user can log in with this status.
     * @return true if login is allowed, false otherwise
     */
    public boolean canLogin() {
        return this == ACTIVE;
    }

    /**
     * Checks if the user needs to verify their email.
     * @return true if email verification is required
     */
    public boolean requiresEmailVerification() {
        return this == PENDING_VERIFICATION || this == VERIFICATION_EXPIRED;
    }

    /**
     * Checks if the account is in a locked state.
     * @return true if account is locked in any way
     */
    public boolean isLocked() {
        return this == LOCKED || this == TEMPORARILY_LOCKED || this == SUSPENDED;
    }

    /**
     * Checks if the account is in an active state (can perform most operations).
     * @return true if account is active or only needs verification
     */
    public boolean isActiveState() {
        return this == ACTIVE || this == PENDING_VERIFICATION || this == VERIFICATION_EXPIRED;
    }

    /**
     * Checks if the account requires administrative intervention.
     * @return true if admin action is needed to resolve the status
     */
    public boolean requiresAdminIntervention() {
        return this == LOCKED || this == SUSPENDED || this == PENDING_DELETION;
    }

    /**
     * Returns the appropriate HTTP status code for this user status.
     * @return HTTP status code
     */
    public int getHttpStatusCode() {
        return switch (this) {
            case ACTIVE -> 200; // OK
            case PENDING_VERIFICATION, VERIFICATION_EXPIRED, REQUIRES_ADDITIONAL_VERIFICATION ->
                    202; // Accepted (but additional action required)
            case LOCKED, TEMPORARILY_LOCKED, SUSPENDED, INACTIVE -> 423; // Locked
            case PENDING_DELETION -> 410; // Gone
            default -> 400; // Bad Request
        };
    }
}
