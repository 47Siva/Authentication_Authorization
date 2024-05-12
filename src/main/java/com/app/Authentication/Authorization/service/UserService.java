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

import com.app.Authentication.Authorization.entity.User;
import com.app.Authentication.Authorization.enumeration.Role;
import com.app.Authentication.Authorization.enumeration.UserStatus;
import com.app.Authentication.Authorization.repository.UserRepository;
import com.app.Authentication.Authorization.request.UserRegisterRequest;
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
		User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

		return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.getPassword())
                .authorities(user.getRole().toString()) // Assign user's role as authority
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();	
        }

	public ResponseEntity<?> getuserDetails(UUID id, String auth) {
		String token =jwtService.extractToken(auth);
		if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization token not provided");
        }
		
		 // Validate token
        if (!jwtService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        }
        
        Optional<User> username = findByUserId(id);
        // Retrieve user details
        UserDetails userDetails = loadUserByUsername(username.get().getUsername());
        
        Optional<User> details = userRepository.findById(id);
		
        // Construct response
        Map<String, Object> response = new HashMap<>();
        if(details.isPresent()) {
        User user = details.get();
    	UserResponse userdetails = UserResponse.builder()
    				                         .email(user.getEmail())
    				                         .userName(user.getUsername())
    				                         .mobileNo(user.getMobileNo())
    				                         .status(user.getStatus())
    				                         .build();
            
        response.put("Authorities", userDetails.getAuthorities());
        response.put("Details", userdetails);
        return ResponseEntity.ok(response);
        }else {
        	response.put("Status", HttpStatus.NO_CONTENT.toString());
        	response.put("message", "User id is invalid pleas check the ID");
        	response.put("Error", HttpStatus.NO_CONTENT);
        	return ResponseEntity.internalServerError().body(response);
        }
	}
}
