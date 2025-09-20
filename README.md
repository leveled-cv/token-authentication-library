# Token Authentication Library

A Spring Boot library that provides `@RequireAuth` annotation for easy authentication across microservices.

[![CI Build](https://github.com/leveled-cv/token-authentication-library/actions/workflows/ci.yml/badge.svg)](https://github.com/shreyjain/token-authentication-lib/actions/workflows/ci.yml)
[![Release](https://github.com/leveled-cv/token-authentication-library/actions/workflows/release.yml/badge.svg)](https://github.com/shreyjain/token-authentication-lib/actions/workflows/release.yml)

## Features

- ✅ `@RequireAuth` annotation for method and class-level authentication
- ✅ Configurable token verification service URL
- ✅ Automatic token extraction from HTTP headers
- ✅ User context available in controllers (username, email, status)
- ✅ Thread-safe authentication context
- ✅ Automatic 403 response for invalid tokens
- ✅ Convenient `User user = AuthUtil.getUser()` access pattern

## Installation

Add this dependency to your microservice's `pom.xml`:

```xml
<dependency>
    <groupId>com.leveledcv</groupId>
    <artifactId>token-authentication-lib</artifactId>
    <version>0.0.1</version>
</dependency>
```

## Configuration

Add the following to your `application.yml` (optional, these are the defaults):

```yaml
auth:
  token-verify-url: http://localhost:8081/token/verify  # Your token verification service
  token-header: Authorization                           # Header name to extract token from
  token-prefix: "Bearer "                              # Token prefix
```

## Usage

### 1. Apply `@RequireAuth` to controllers

```java
@RestController
@RequestMapping("/api")
public class MyController {
    
    @RequireAuth
    @GetMapping("/protected")
    public ResponseEntity<String> protectedEndpoint() {
        User user = AuthUtil.getUser();
        
        return ResponseEntity.ok("Hello " + user.username + " (" + user.email + ")");
    }
    
    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.ok("This endpoint is public");
    }
}

// Apply to entire class for all methods
@RequireAuth
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    // All methods require authentication
    
    @GetMapping("/dashboard")
    public ResponseEntity<String> adminDashboard() {
        User user = AuthUtil.getUser();
        return ResponseEntity.ok("Admin dashboard - user: " + user.username);
    }
}
```

### 2. Access User Information

```java
@RequireAuth
@GetMapping("/me")
public ResponseEntity<Map<String, Object>> getCurrentUser() {
    User user = AuthUtil.getUser();
    
    Map<String, Object> response = new HashMap<>();
    response.put("username", user.username);
    response.put("email", user.email);
    response.put("status", user.status);
    response.put("isActive", user.isActive());
    response.put("canLogin", user.canLogin());
    response.put("isLocked", user.isLocked());
    
    return ResponseEntity.ok(response);
}
```

## How It Works

1. **Token Extraction**: Automatically extracts token from `Authorization` header
2. **Verification Call**: Makes GET request to your token verification service
3. **Response Handling**: 
   - If response contains `{"error": "Session not found"}` → Returns 403 Forbidden
   - If successful → Parses user info and makes it available via `AuthUtil`
4. **Context Management**: User info is stored in ThreadLocal for the request duration

## Token Verification Service Response Format

Your token verification service should return:

**Success Response:**
```json
{
  "username": "john_doe",
  "email": "john@example.com", 
  "status": "ACTIVE"
}
```

**Error Response:**
```json
{
  "error": "Session not found"
}
```

## User Status Enum

The library includes a comprehensive `UserStatus` enum:

- `ACTIVE` - User can access all features
- `PENDING_VERIFICATION` - Email verification required
- `LOCKED` - Account locked by admin
- `TEMPORARILY_LOCKED` - Locked due to failed attempts
- `SUSPENDED` - Account suspended
- `INACTIVE` - Account deactivated
- `PENDING_DELETION` - Account being deleted
- `VERIFICATION_EXPIRED` - Email verification expired
- `REQUIRES_ADDITIONAL_VERIFICATION` - Additional verification needed

## User Object Methods

The `User` object provides convenient methods:

```java
User user = AuthUtil.getUser();

// Direct field access
String username = user.username;
String email = user.email;
UserStatus status = user.status;

// Convenience methods
boolean isActive = user.isActive();         // Status is ACTIVE
boolean canLogin = user.canLogin();         // Can log in with current status
boolean isLocked = user.isLocked();         // Account is locked in any way
```

## Error Handling

The library automatically handles:

- **Missing Authorization header** → 403 Forbidden
- **Invalid/expired tokens** → 403 Forbidden  
- **Token verification service errors** → 500 Internal Server Error

## Thread Safety

User context is stored in `ThreadLocal`, making it safe for concurrent requests while ensuring user information is only available within the current request scope.

## Development

### Building

```bash
./mvnw clean package
```

### Running Tests

```bash
./mvnw test
```

## License

[Add your license here]

## Contributing

[Add contributing guidelines here]
