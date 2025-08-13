package com.app.Authentication.Authorization.security;


import static com.app.Authentication.Authorization.util.Constants.HEADER_STRING;
import static com.app.Authentication.Authorization.util.Constants.TOKEN_PREFIX;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.app.Authentication.Authorization.response.MessageService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


//@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	MessageService messageSource;
	
//	@Autowired
	private  JwtService jwtService;
//	@Autowired
	private  UserDetailsService userDetailsService;
	
	private  HandlerExceptionResolver exceptionResolver;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

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
//				throw new ServletException("Missing or invalid Authorization header");
			}
			
			jwttoken = authHeader.substring(7).trim();
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
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			exceptionResolver.resolveException(request, response, null, e);
//			
//	        logger.error("Exception occurred: ", e);
//	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set appropriate status
//	        response.getWriter().write("Unauthorized: " + e.getMessage()); // Provide error message
//	        response.getWriter().flush();
		}
	}

	
}
