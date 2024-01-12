package com.bookingservice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {


//     @Autowired
//     public void registerAuthProvider(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication();
//      }
     @Bean
     public InMemoryUserDetailsManager userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder) {
         InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
         manager.createUser(User.withUsername("user")
                 .password(bCryptPasswordEncoder.encode("password"))
                 .roles("USER")
                 .build());
         return manager;
     }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         http.authorizeHttpRequests((auth) -> auth.requestMatchers(new AntPathRequestMatcher("/bookings"),new AntPathRequestMatcher("/delete/**")).permitAll()
                                 .requestMatchers(HttpMethod.GET, "/booking-admin").hasRole("ADMIN")

                          //.requestMatchers(HttpMethod.GET,"/booking-service").hasRole("ANY")
                         .requestMatchers(HttpMethod.GET,"/login").permitAll()
                        )
                      //  .requestMatchers(HttpMethod.PATCH, "/bookings/*").hasRole("ADMIN")
                      //  .requestMatchers(HttpMethod.DELETE, "/bookings/*").hasRole("ADMIN"))
                  .formLogin(Customizer.withDefaults())
                .csrf().disable();
         http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
         http.sessionManagement().sessionFixation().migrateSession();
         return http.build();
    }




}



