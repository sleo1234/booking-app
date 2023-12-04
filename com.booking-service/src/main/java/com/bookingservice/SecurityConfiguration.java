package com.bookingservice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {


    @Autowired
    public void registerAuthProvider(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         http.authorizeHttpRequests((auth) -> auth.requestMatchers(new AntPathRequestMatcher("/bookings")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/booking-service/")).permitAll()
                        .requestMatchers(HttpMethod.POST, "/bookings").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/bookings/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/bookings/*").hasRole("ADMIN")).formLogin(Customizer.withDefaults())
                .csrf().disable();

         return http.build();
    }




}



