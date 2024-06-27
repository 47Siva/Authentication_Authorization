package com.app.Authentication.Authorization.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.app.Authentication.Authorization.advice.SignatureException;
import com.app.Authentication.Authorization.entity.User;
import com.app.Authentication.Authorization.enumeration.Role;
import com.app.Authentication.Authorization.enumeration.Status;
import com.app.Authentication.Authorization.repository.UserRepository;
import com.app.Authentication.Authorization.response.UserResponse;
import com.app.Authentication.Authorization.security.JwtService;
import com.app.Authentication.Authorization.util.PasswordUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final UserRepository userRepository;
	private final JwtService jwtService;
	private final UserService userService;

	public User createAdmin(User user) {
		var adminobj = User.builder().email(user.getEmail()).mobileNo(user.getMobileNo())
				.password(PasswordUtil.getEncryptedPassword(user.getPassword())).userRole(Role.ADMIN)
				.status(Status.ACTIVE).userName(user.getUsername()).build();
		return userRepository.saveAndFlush(adminobj);
	}

	public ResponseEntity<?> getadmindetials(String useremail, String auth) {

		try {
			String token = jwtService.extractToken(auth);
			// Construct response
			Map<String, Object> response = new HashMap<>();
			// Validate token
			if (!jwtService.validateToken(token)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
			}
			Optional<User> userdetails = userRepository.findByUserEmail(useremail);
			if (userdetails.isPresent()) {
				User user = userdetails.get();
				UserResponse userresponse = UserResponse.builder().email(user.getEmail()).userName(user.getUsername())
						.mobileNo(user.getMobileNo()).status(user.getStatus()).userRole(user.getUserRole()).build();

				// Retrieve user details
				UserDetails userDetails = userService.loadUserByUsername(userdetails.get().getUsername());
				response.put("Authorities", userDetails.getAuthorities());
				response.put("Details", userresponse);
				return ResponseEntity.ok(response);
			} else {
				response.put("Status", HttpStatus.NO_CONTENT.toString());
				response.put("message", "User id is invalid pleas check the ID");
				response.put("Error", HttpStatus.NO_CONTENT);
				return ResponseEntity.internalServerError().body(response);
			}
		} catch (SignatureException e) {
			e.printStackTrace();
			throw new SignatureException("Token is invalid");
		}
	}

}
