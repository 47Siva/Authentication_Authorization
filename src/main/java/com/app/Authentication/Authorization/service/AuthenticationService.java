package com.app.Authentication.Authorization.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.Authentication.Authorization.dto.ErrorDto;
import com.app.Authentication.Authorization.entity.User;
import com.app.Authentication.Authorization.repository.UserRepository;
import com.app.Authentication.Authorization.request.AuthenticationRequest;
import com.app.Authentication.Authorization.security.JwtService;
import com.app.Authentication.Authorization.util.PasswordUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final JwtService jwtService;

	private final UserRepository userRepository;

	public ResponseEntity<?> userlogin(AuthenticationRequest request) {

		ErrorDto errorDto = null;
		Map<String, Object> response = new HashMap<String, Object>();
		if (null == request) {
			errorDto = new ErrorDto();
			errorDto.setCode("400");
			errorDto.setMessage("Invalid request payload.!");
			response.put("status", 0);
			response.put("error", errorDto);
			return ResponseEntity.badRequest().body(response);
		}

		Optional<User> userOptional = userRepository.findByUserName(request.getUserName());
		if (!userOptional.isPresent()) {
			errorDto = new ErrorDto();
			errorDto.setCode("400");
			errorDto.setMessage("Usernot found.!");
			response.put("status", 0);
			response.put("error", errorDto);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}

		User user = userOptional.get();
		String enPassword = PasswordUtil.getEncryptedPassword(request.getPassword());

		if (!user.getUsername().equals(request.getUserName())) {
			errorDto = new ErrorDto();
			errorDto.setCode("400");
			errorDto.setMessage("Invalid username.!");
			response.put("status", 0);
			response.put("error", errorDto);
			return ResponseEntity.badRequest().body(response);
		}

		if (!user.getPassword().equals(enPassword)) {
			errorDto = new ErrorDto();
			errorDto.setCode("400");
			errorDto.setMessage("Password is wrong");
			response.put("Status", "0");
			response.put("error", errorDto);

			return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response); 
		}

		final String token = jwtService.generateToken(user);
		response.put("Status", 1);
		response.put("message", "Logged in successfully.!");
		response.put("jwt", token);
		response.put("userId", user.getId());
		response.put("userName", user.getUsername());
		response.put("role", user.getRole());

		return ResponseEntity.ok().body(response);
	}

}
