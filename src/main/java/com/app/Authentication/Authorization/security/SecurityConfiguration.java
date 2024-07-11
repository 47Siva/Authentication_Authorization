package com.app.Authentication.Authorization.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class  SecurityConfiguration {
   
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    
//    @Qualifier("HandlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;
    
    @Autowired
    public SecurityConfiguration(AuthenticationProvider authenticationProvider, JwtService jwtService,
                                 UserDetailsService userDetailsService, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        this.authenticationProvider = authenticationProvider;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.exceptionResolver = exceptionResolver;
    }
    
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService, userDetailsService, exceptionResolver);
    }
    
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
                        		  "/swagger-ui.html",
                        		  "/api/invoice/**",
                        		  "/api/product/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }
    
   
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth ->
                auth.requestMatchers("api/user/**").hasAnyRole("USER","ADMIN") // Allow access to public endpoints
                .requestMatchers("api/admin/**").hasRole("ADMIN") // Require ADMIN role for /admin endpoints
                .anyRequest().authenticated()) // Require authentication for any other endpoint
            
            .formLogin(form -> form // Enable form-based login
                .permitAll()) // Allow anyone to access login page
            .logout(logut -> logut // Enable logout
                .logoutSuccessUrl("/login?logout") // Redirect to login page after logout
                .permitAll()); // Allow anyone to access logout endpoint
        
    }

}
