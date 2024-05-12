package com.app.Authentication.Authorization.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Authentication.Authorization.entity.User;
import com.app.Authentication.Authorization.enumeration.RequestType;
import com.app.Authentication.Authorization.repository.UserRepository;
import com.app.Authentication.Authorization.request.AdminRegister;
import com.app.Authentication.Authorization.request.AuthenticationRequest;
import com.app.Authentication.Authorization.request.UserRegisterRequest;
import com.app.Authentication.Authorization.response.ResponseGenerator;
import com.app.Authentication.Authorization.response.TransactionContext;
import com.app.Authentication.Authorization.security.JwtService;
import com.app.Authentication.Authorization.service.AdminService;
import com.app.Authentication.Authorization.service.AuthenticationService;
import com.app.Authentication.Authorization.service.UserService;
import com.app.Authentication.Authorization.validator.AdminValidator;
import com.app.Authentication.Authorization.validator.RegisterValidator;
import com.app.Authentication.Authorization.validator.ValidationResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication_Controller", description = "Defines endpoints that can be hit only when the User is not logged in..")
public class AuthenticationController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final JwtService jwtService;
	private final UserService userService;
	private final AuthenticationService authenticationService;
	private final UserRepository userRepository;
	private final RegisterValidator registerValidator;
	private final AdminValidator adminValidator;  
	private final ResponseGenerator responseGenerator;
	private final AdminService adminService;

	@Operation(description = "Post End Point", summary = "This is a User register api", responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Unauthorized / Invalid token", responseCode = "401") })
	@PostMapping(value = "/user/register", produces = "application/json")
	public ResponseEntity<?> userRegister(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Signup request payload") @RequestBody UserRegisterRequest userRegisterRequest,
			@RequestHeader HttpHeaders httpHeaders) throws Exception {

		ValidationResult validationResult = registerValidator.validate(RequestType.POST, userRegisterRequest);
		User user = (User) validationResult.getObject();

		User userservice = userService.createUser(user);

		Map<String, Object> response = new HashMap<>();
		final String token = jwtService.generateToken(userservice);
		response.put("Status", 1);
		response.put("message", "You have register successfully.");
		response.put("token", token);

		TransactionContext context = responseGenerator.generateTransationContext(httpHeaders);
		try {
			return responseGenerator.successResponse(context, response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@Operation(description = "Post End Point", summary = "This is a Admin register api", responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Unauthorized / Invalid token", responseCode = "401") })
	@PostMapping(value = "/admin/register", produces = "application/json")
	public ResponseEntity<?> adminRegister(@RequestBody AdminRegister register,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Admin Signup request payload")
			@RequestHeader HttpHeaders httpHeaders){
		ValidationResult validationResult = adminValidator.validate(RequestType.POST, register);
		User user = (User) validationResult.getObject();
		
		User adminservice = adminService.createAdmin(user);
		Map<String, Object> response = new HashMap<>();
		final String token = jwtService.generateToken(adminservice);
		response.put("Status", 1);
		response.put("message", "You have register successfully.");
		response.put("token", token);

		TransactionContext context = responseGenerator.generateTransationContext(httpHeaders);
		try {
			return responseGenerator.successResponse(context, response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(description = "Get End Point", summary = "This is a User login api", responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Unauthorized / Invalid token", responseCode = "401") })
	@PostMapping(value = "/user/login", produces = "application/json")
	public ResponseEntity<?> userLogin(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Signup request payload") @RequestBody AuthenticationRequest request,
			@RequestHeader HttpHeaders httpHeaders) {

		ResponseEntity<?> authenticationservice = authenticationService.userlogin(request);

		TransactionContext context = responseGenerator.generateTransationContext(httpHeaders);
		try {
			return responseGenerator.successResponse(context, authenticationservice, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.UNAUTHORIZED);
		}

	}
	
	
	
}
