package com.myapp.shoppingmall.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auths -> auths
                .requestMatchers("/category/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                .anyRequest().permitAll())
                .formLogin(form -> form
                        .loginPage("/login"))
                .logout(form -> form
                        .logoutSuccessUrl("/"))
                .exceptionHandling(exp -> exp
                        .accessDeniedPage("/"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }

    @Bean
    public UserDetailsService userDetailsServiceImpl() {
        return new UserDetailsServiceImpl();
    }

}
