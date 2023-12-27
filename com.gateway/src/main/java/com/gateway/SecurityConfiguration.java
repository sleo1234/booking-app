package com.gateway;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;

import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebFluxSecurity

public class SecurityConfiguration {


    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
                .password(encoder().encode("password"))
                .roles("USER")
                .build();
        UserDetails adminUser = User.withUsername("admin")
                .password(encoder().encode("admin"))
                .roles("ADMIN")
                .build();
        return new MapReactiveUserDetailsService(user, adminUser);
    }
 //   @Bean
//    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//        http.formLogin()
//                .authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("/home"))
//                .and().authorizeExchange()
//                .pathMatchers("/booking-service/**", "/rating-service/**", "/login*", "/")
//                .permitAll()
//                .pathMatchers("/eureka/**").hasRole("ADMIN")
//                .anyExchange().authenticated().and()
//                .logout().and().csrf().disable().httpBasic(withDefaults());
//
//
//        return http.build();
//    }
 @Bean
 public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
     http
             .authorizeExchange(exchanges ->
                     exchanges
                             .pathMatchers( "/rating-service/**", "/login*", "/bookings").permitAll()
                             .pathMatchers("/eureka/**").hasRole("ADMIN")
                             .pathMatchers("/booking-admin").hasRole("ADMIN")
                             .anyExchange().authenticated()
             )
             .formLogin(formLogin ->
                     formLogin.authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("/home"))
             )
             .logout(logout ->
                     logout.logoutSuccessHandler(new RedirectServerLogoutSuccessHandler())
             )
             .csrf(c -> c.disable())
             .httpBasic();

     return http.build();
 }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


//    @Bean
//    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) throws Exception {
//
//        http.formLogin(f -> f.authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("/index.html")));
//
//
//
//        http.authorizeExchange((auth) -> auth.pathMatchers("/admin/").hasRole("ADMIN")).httpBasic(withDefaults())
//                .formLogin(withDefaults());;
//        http.authorizeExchange(auth -> auth.pathMatchers("/booking-service/**", "/login*", "/").permitAll());
//
//
//        return http.build();
//    }


}