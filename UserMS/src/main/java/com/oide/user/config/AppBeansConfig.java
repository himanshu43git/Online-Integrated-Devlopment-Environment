package com.oide.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppBeansConfig {

    /**
     * PasswordEncoder is now here, not in SecurityConfig,
     * so UserServiceImpl can depend on it without pulling in SecurityConfig.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
