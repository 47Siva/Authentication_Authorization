package com.app.Authentication.Authorization.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Authentication.Authorization.entity.User;
import com.app.Authentication.Authorization.enumeration.RequestType;
import com.app.Authentication.Authorization.enumeration.Status;
import com.app.Authentication.Authorization.request.UserRegisterRequest;
import com.app.Authentication.Authorization.response.MessageService;
import com.app.Authentication.Authorization.response.ResponseGenerator;
import com.app.Authentication.Authorization.response.TransactionContext;
import com.app.Authentication.Authorization.security.JwtService;
import com.app.Authentication.Authorization.service.UserService;
import com.app.Authentication.Authorization.util.ResponseMessage;
import com.app.Authentication.Authorization.validator.RegisterValidator;
import com.app.Authentication.Authorization.validator.UserValidator;
import com.app.Authentication.Authorization.validator.ValidationResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "User_Controller", description = "Hittable endpoints are welcome after user login")
public class UserController {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final JwtService jwtService;
	private final UserService userService;
	private final UserValidator userValidator;
	private final ResponseGenerator responseGenerator;
	private @NonNull MessageService messagePropertySource;


	@Operation(description = "Get End Point", summary = "Allows to fetch user by user name.", responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Unauthorized / Invalid token", responseCode = "401") })
	@GetMapping(value = "/getUser/{username}", produces = "application/json")
	public ResponseEntity<?>getUser(@PathVariable ("username") String username ,@RequestHeader("Authorization") String auth){
		
		 return userService.getuserDetailsUserNameFromToken(username , auth);
	}
	
	@Operation(description = "PUT End Point", summary = "Allows to update existing user.", responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Unauthorized / Invalid token", responseCode = "401") })
	@PutMapping(value = "/update",  produces = "application/json")
	public ResponseEntity<?> update(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Signup request payload")
	        @RequestBody UserRegisterRequest request,
			@RequestHeader HttpHeaders httpHeader,
			@RequestHeader ("Authorization")String auth) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		ValidationResult validationResult = userValidator.validate(RequestType.PUT, request,auth);
		userService.saveOrUpdate((User) validationResult.getObject());
		try {
			return responseGenerator.successResponse(context, messagePropertySource.messageResponse("user.update"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@Operation(description = "Delete End Point", summary = "Allows to delete use by user name.", responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Unauthorized / Invalid token", responseCode = "401") })
	@DeleteMapping(value =  "/deleteUser/{username}")
	public ResponseEntity<?>DeleteUser(@PathVariable("username") String username,
			                            @RequestHeader HttpHeaders httpHeader){
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			User userObject = userService.findByUserName(username).get();

			if (null == userObject) {
				return responseGenerator.errorResponse(context, ResponseMessage.INVALID_OBJECT_REFERENCE,
						HttpStatus.BAD_REQUEST);
			}
//			userObject.setStatus(Status.INACTIVE);
//			userService.saveOrUpdate(userObject);
			userService.deleteUser(userObject);

			return responseGenerator.successResponse(context, messagePropertySource.messageResponse("user.delete"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, messagePropertySource.messageResponse("user.invalid.delete"),
					HttpStatus.BAD_REQUEST);

		}
	}
}
