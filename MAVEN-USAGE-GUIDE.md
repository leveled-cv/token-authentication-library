# Token Authentication Library - Maven Package Usage Guide

## 🎉 Now Available as Maven Package!

Your token authentication library is now configured to be published to GitHub Packages automatically when you create a release. This means you can use it in any microservice just like any other Maven dependency!

## 🚀 How to Use in Your Microservices

### Step 1: Add the Repository to Your Microservice's `pom.xml`

```xml
<repositories>
    <repository>
        <id>github</id>
        <name>GitHub Packages</name>
        <url>https://maven.pkg.github.com/leveled-cv/token-authentication-library</url>
    </repository>
</repositories>
```

### Step 2: Add the Dependency

```xml
<dependency>
    <groupId>com.leveledcv</groupId>
    <artifactId>token-authentication-lib</artifactId>
    <version>0.0.1</version> <!-- Use the latest released version -->
</dependency>
```

### Step 3: Configure Your Microservice

Add to your `application.yml`:

```yaml
auth:
  token-verify-url: http://localhost:8081/token/verify
  token-header: Authorization
  token-prefix: "Bearer "
```

### Step 4: Use the Authentication

```java
@RestController
@RequestMapping("/api")
public class MyController {
    
    @RequireAuth
    @GetMapping("/protected")
    public ResponseEntity<String> protectedEndpoint() {
        User user = AuthUtil.getUser();
        return ResponseEntity.ok("Hello " + user.getUsername());
    }
    
    @RequireAuth
    @PostMapping("/admin")
    public ResponseEntity<String> adminEndpoint() {
        User user = AuthUtil.getUser();
        if (!"ACTIVE".equals(user.getStatus())) {
            return ResponseEntity.status(403).body("Account not active");
        }
        return ResponseEntity.ok("Admin access granted");
    }
}
```

## 🔧 Authentication Configuration for Private Packages

Since this is published to GitHub Packages, you'll need to authenticate with GitHub to download the package. Add this to your `~/.m2/settings.xml`:

```xml
<settings>
    <servers>
        <server>
            <id>github</id>
            <username>YOUR_GITHUB_USERNAME</username>
            <password>YOUR_GITHUB_TOKEN</password>
        </server>
    </servers>
</settings>
```

**Note:** Create a GitHub Personal Access Token with `read:packages` permission.

## 🏗️ For CI/CD Deployments

In your GitHub Actions workflow for microservices:

```yaml
- name: Set up Maven authentication
  run: |
    echo "<settings><servers><server><id>github</id><username>${{ github.actor }}</username><password>${{ secrets.GITHUB_TOKEN }}</password></server></servers></settings>" > ~/.m2/settings.xml

- name: Build microservice
  run: mvn clean package
```

## 🌍 Alternative: Maven Central (For Public Access)

If you want to make it globally available without authentication, we can also set up Maven Central publishing. This would make it available like any other public Maven dependency (Spring Boot, Jackson, etc.).

## 📝 Complete Example Microservice `pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.leveledcv</groupId>
    <artifactId>my-microservice</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.6</version>
    </parent>
    
    <repositories>
        <repository>
            <id>github</id>
            <name>GitHub Packages</name>
            <url>https://maven.pkg.github.com/leveled-cv/token-authentication-library</url>
        </repository>
    </repositories>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <!-- Your token authentication library -->
        <dependency>
            <groupId>com.leveledcv</groupId>
            <artifactId>token-authentication-lib</artifactId>
            <version>0.0.1</version>
        </dependency>
    </dependencies>
</project>
```

That's it! No more manual JAR copying, no more complex deployment scripts. Just add the dependency and it works everywhere! 🎊
