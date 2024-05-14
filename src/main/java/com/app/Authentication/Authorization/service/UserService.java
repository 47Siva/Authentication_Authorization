package com.app.Authentication.Authorization.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.Authentication.Authorization.advice.MalformedJwtException;
import com.app.Authentication.Authorization.entity.User;
import com.app.Authentication.Authorization.enumeration.Role;
import com.app.Authentication.Authorization.enumeration.UserStatus;
import com.app.Authentication.Authorization.repository.UserRepository;
import com.app.Authentication.Authorization.response.UserResponse;
import com.app.Authentication.Authorization.security.JwtService;
import com.app.Authentication.Authorization.util.PasswordUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;
	private final JwtService jwtService;

	public User createUser(User user) {
		var userobj = User.builder().email(user.getEmail()).mobileNo(user.getMobileNo())
				.password(PasswordUtil.getEncryptedPassword(user.getPassword())).role(Role.USER)
				.status(UserStatus.ACTIVE).userName(user.getUsername()).build();

		return userRepository.saveAndFlush(userobj);
	}

	public Optional<User> findByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}

	public Optional<User> findByDuplicateEmail(String email) {

		return userRepository.findByDuplicateEamil(email);
	}

	public Optional<User> findByDuplicateUserName(String userName) {
		return userRepository.findByDuplicateUserName(userName);
	}

	public Optional<User> getMobileNos(String phoneNumber) {
		return userRepository.findByDuplicateNumber(phoneNumber);
	}

	public Optional<User> findByDuplicatePassword(String password) {

		String enpassword = PasswordUtil.getEncryptedPassword(password);

		return userRepository.findByDuplicatePassword(enpassword);
	}

	public Optional<User> findByUserId(UUID id) {
		return userRepository.findById(id);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOptional = userRepository.findByUserName(username);
		if (!userOptional.isPresent()) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		User user = userOptional.get();
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				user.getAuthorities());
	}

	public ResponseEntity<?> getuserDetailsUserNameFromToken(String username, String auth) {
		try {
			String token = jwtService.extractToken(auth);
			// Validate token
			if (!jwtService.validateToken(token)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
			}
			Optional<User> userdetails = userRepository.findByUserName(username);
			// Retrieve user details
			UserDetails userDetails = loadUserByUsername(userdetails.get().getUsername());
			Optional<User> details = userRepository.findById(userdetails.get().getId());
			
			// Construct response
			Map<String, Object> response = new HashMap<>();
			if(!details.get().getRole().equals(Role.USER)) {
				response.put("Status",HttpStatus.NOT_ACCEPTABLE.toString());
				response.put("message", "User can't access the request");
				response.put("Error", HttpStatus.NOT_ACCEPTABLE);
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
			}else if (details.isPresent()) {
				User user = details.get();
				UserResponse userresponse = UserResponse.builder().email(user.getEmail()).userName(user.getUsername())
						.mobileNo(user.getMobileNo()).status(user.getStatus()).build();

				response.put("Authorities", userDetails.getAuthorities());
				response.put("Details", userresponse);
				return ResponseEntity.ok(response);
			}else {
				response.put("Status", HttpStatus.NO_CONTENT.toString());
				response.put("message", "User id is invalid pleas check the ID");
				response.put("Error", HttpStatus.NO_CONTENT);
				return ResponseEntity.internalServerError().body(response);
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new MalformedJwtException("Token is invalid");
		}
	}

	public ResponseEntity<?> getuserdetials(String username) {
		Optional<User> details = userRepository.findByUserName(username);
		if (details.isPresent()) {
			User user = details.get();
			UserResponse response = UserResponse.builder().email(user.getEmail()).userName(user.getUsername())
					.mobileNo(user.getMobileNo()).status(user.getStatus()).build();
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not found");
		}
	}
}
