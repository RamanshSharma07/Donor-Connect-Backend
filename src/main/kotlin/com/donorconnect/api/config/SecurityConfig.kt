package com.donorconnect.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // 1. Disable CSRF (Cross-Site Request Forgery)
            // CSRF protection is for browser-based apps. Since we are building a stateless REST API for Android, we turn it off.
            .csrf { it.disable() }

            // 2. Configure Endpoint Permissions
            .authorizeHttpRequests { auth ->
                auth
                    // Leave the auth folder public so users can actually register and login
                    .requestMatchers("/api/v1/auth/**").permitAll()
                    // Require authentication (a valid JWT, eventually) for everything else
                    .anyRequest().authenticated()
            }

        return http.build()
    }
}