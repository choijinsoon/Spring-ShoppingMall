package com.myapp.shoppingmall.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth->auth
            // .requestMatchers("/category/**").hasAnyRole("USER", "ADMIN")
            // .requestMatchers("/admin/**").hasAnyRole("ADMIN")
            .requestMatchers("/").permitAll()
        );
        
        return http.build();
    }
}
