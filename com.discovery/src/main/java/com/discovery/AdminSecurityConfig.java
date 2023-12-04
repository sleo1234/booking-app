package com.discovery;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AdminSecurityConfig {

    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.sessionManagement().
        sessionCreationPolicy(SessionCreationPolicy.NEVER);
        http.httpBasic().disable().authorizeHttpRequests().
                requestMatchers(HttpMethod.GET,"/").
                hasRole("ADMIN")
                .requestMatchers("/info","/health")
                .authenticated()
                .anyRequest().
                denyAll().and().csrf().disable();

        return http.build();

    }
}
