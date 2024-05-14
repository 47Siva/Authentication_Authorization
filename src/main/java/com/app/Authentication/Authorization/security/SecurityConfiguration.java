package com.app.Authentication.Authorization.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.app.Authentication.Authorization.service.UserService;
import com.app.Authentication.Authorization.util.PasswordUtil;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true) // Enables method level security
public class  SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("api/auth/**",
                        		  "/v2/api-docs",
                        		  "/v3/api-docs",
                        		  "/v3/api-docs/**",
                        		  "/swagger-resources",
                        		  "/swagger-resources/**",
                        		  "/swagger-ui/**",
                        		  "/configuration/ui",
                        		  "/configuration/security",
                        		  "/webjars/**",
                        		  "/swagger-ui.html")
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }
    
   
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth ->
                auth.requestMatchers("api/user/**").permitAll() // Allow access to public endpoints
                .requestMatchers("api/admin/**").hasRole("ADMIN") // Require ADMIN role for /admin endpoints
                .anyRequest().authenticated()) // Require authentication for any other endpoint
            
            .formLogin(form -> form // Enable form-based login
                .permitAll()) // Allow anyone to access login page
            .logout(logut -> logut // Enable logout
                .logoutSuccessUrl("/login?logout") // Redirect to login page after logout
                .permitAll()); // Allow anyone to access logout endpoint
        
    }

}
