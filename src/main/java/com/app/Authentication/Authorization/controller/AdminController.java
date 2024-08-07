package com.app.Authentication.Authorization.controller;

import static com.app.Authentication.Authorization.util.Constants.TOKEN_PREFIX;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Authentication.Authorization.response.MessageService;
import com.app.Authentication.Authorization.response.ResponseGenerator;
import com.app.Authentication.Authorization.security.JwtService;
import com.app.Authentication.Authorization.service.AdminService;
import com.app.Authentication.Authorization.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "Admin_Controller", description = "Hittable endpoints are welcome after admin login")
public class AdminController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	MessageService messageSource;
	private final JwtService jwtService;
	private final AdminService adminService;
	private final UserService userService;
	private final ResponseGenerator responseGenerator;

	@Operation(description = "Get End Point", summary = "This is a Token Validation and Get All User Can see Admin Only", responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Unauthorized / Invalid token", responseCode = "401") })
	@GetMapping("/getAllusers")
	public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String auth) throws ServletException {
		if (auth == null) {
			throw new ServletException(messageSource.messageResponse("token.missing"));
		}
		if (!auth.startsWith(TOKEN_PREFIX)) {
			throw new ServletException(messageSource.messageResponse("token.invalid"));
		}
		return userService.getAllUsers(auth);
	}

	@Operation(description = "Get End Point", summary = "This is a Token Validation and Get User by email api", responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Unauthorized / Invalid token", responseCode = "401") })
	@GetMapping(value = "/getUser/{useremail}")
	public ResponseEntity<?> getUser(@PathVariable("useremail") String useremail,
			@RequestHeader("Authorization") String auth) {

		return adminService.getadmindetials(useremail, auth);
	}
}
