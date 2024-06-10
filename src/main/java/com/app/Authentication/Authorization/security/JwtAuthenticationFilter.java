package com.app.Authentication.Authorization.security;


import static com.app.Authentication.Authorization.util.Constants.TOKEN_PREFIX;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.app.Authentication.Authorization.advice.SignatureException;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.app.Authentication.Authorization.util.Constants.*;


//@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

//	@Autowired
	private  JwtService jwtService;
//	@Autowired
	private  UserDetailsService userDetailsService;
	
	private  HandlerExceptionResolver exceptionResolver;
	
	@Autowired
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService, HandlerExceptionResolver exceptionResolver) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.exceptionResolver = exceptionResolver;
    }
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		final String authHeader = request.getHeader(HEADER_STRING);
		String jwttoken;
		String userName;
		try {
			if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
				filterChain.doFilter(request, response);
				return;
			}
			jwttoken = authHeader.substring(7);
			userName = jwtService.extractUserName(jwttoken);
			if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
				if (jwtService.isTokenValid(jwttoken, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}

			filterChain.doFilter(request, response);
		} catch (Exception e) {
			exceptionResolver.resolveException(request, response, null, e);
		}
	}

	
}
