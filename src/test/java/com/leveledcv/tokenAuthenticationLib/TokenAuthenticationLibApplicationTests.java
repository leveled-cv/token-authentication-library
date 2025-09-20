package com.leveledcv.tokenAuthenticationLib;

import com.leveledcv.tokenAuthenticationLib.config.AuthenticationAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * Basic test to verify the authentication library configuration loads correctly
 */
@SpringBootTest
@ContextConfiguration(classes = AuthenticationAutoConfiguration.class)
class TokenAuthenticationLibApplicationTests {

    @Test
    void contextLoads() {
        // This test verifies that the Spring context loads successfully
        // with our authentication auto-configuration
    }
}
