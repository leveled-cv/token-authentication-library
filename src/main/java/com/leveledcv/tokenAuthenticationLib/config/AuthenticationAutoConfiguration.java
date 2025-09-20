package com.leveledcv.tokenAuthenticationLib.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;

/**
 * Auto-configuration for the authentication library
 */
@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties(AuthProperties.class)
@ComponentScan("com.leveledcv.tokenAuthenticationLib")
public class AuthenticationAutoConfiguration {

    /**
     * Provides a RestTemplate bean for making HTTP requests to the token verification service
     */
    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
