package com.example.microservice.controller;

import com.leveledcv.tokenAuthenticationLib.annotations.RequireAuth;
import com.leveledcv.tokenAuthenticationLib.models.User;
import com.leveledcv.tokenAuthenticationLib.models.enums.UserStatus;
import com.leveledcv.tokenAuthenticationLib.util.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Example controller showing how to use the authentication library
 */
@RestController
@RequestMapping("/api")
public class ExampleController {

    // Method-level authentication - NEW CONVENIENT WAY
    @RequireAuth
    @GetMapping("/protected")
    public ResponseEntity<String> protectedEndpoint() {
        User user = AuthUtil.getUser();

        return ResponseEntity.ok("Hello " + user.username + " (" + user.email + ") - Status: " + user.status);
    }

    // Get current user info - NEW CONVENIENT WAY
    @RequireAuth
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        User user = AuthUtil.getUser();

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", user.username);
        userInfo.put("email", user.email);
        userInfo.put("status", user.status);
        userInfo.put("isActive", user.isActive()); // Using convenience method
        userInfo.put("canLogin", user.canLogin()); // Using convenience method
        userInfo.put("isLocked", user.isLocked()); // Using convenience method

        return ResponseEntity.ok(userInfo);
    }

    // Example showing different ways to access user info
    @RequireAuth
    @GetMapping("/profile")
    public ResponseEntity<String> getUserProfile() {
        User user = AuthUtil.getUser();

        // Direct field access
        String message = "Profile for: " + user.username;

        // Using convenience methods
        if (user.isActive()) {
            message += " (Active User)";
        } else if (user.isLocked()) {
            message += " (Account Locked)";
        }

        return ResponseEntity.ok(message);
    }

    // Public endpoint (no authentication required)
    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.ok("This endpoint is public - no authentication required");
    }
}

// Class-level authentication (all methods require auth)
@RequireAuth
@RestController
@RequestMapping("/api/admin")
class AdminController {

    @GetMapping("/dashboard")
    public ResponseEntity<String> adminDashboard() {
        User user = AuthUtil.getUser();
        return ResponseEntity.ok("Admin dashboard - user: " + user.username);
    }

    @PostMapping("/action")
    public ResponseEntity<String> adminAction() {
        User user = AuthUtil.getUser();
        return ResponseEntity.ok("Admin action performed by: " + user.username);
    }
}
