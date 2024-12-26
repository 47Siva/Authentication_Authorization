package com.app.Authentication.Authorization.security;


import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.app.Authentication.Authorization.advice.UsernameNotFoundException;
import com.app.Authentication.Authorization.entity.User;
import com.app.Authentication.Authorization.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

//    @Bean
//    public UserDetailsService userDetailsService() {
//    	
//    	
//        return username ->  userRepository.findByUserName(username)
//                .orElseThrow(() -> new UsernameNotFoundException("user name not found..! from token"));
//
//    }
    @Bean
    public UserDetailsService userDetailsService() {

        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        		Optional<User> userOptional = userRepository.findByUserEmail(username);
        		if (!userOptional.isPresent()) {
        			throw new UsernameNotFoundException("User not found from the token with username: " + username);
        		}
        		User user = userOptional.get();
        		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
        				user.getAuthorities());
        	}
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
}

