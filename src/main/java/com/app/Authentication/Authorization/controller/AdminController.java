package com.app.Authentication.Authorization.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Authentication.Authorization.response.ResponseGenerator;
import com.app.Authentication.Authorization.security.JwtService;
import com.app.Authentication.Authorization.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "Admin_Controller", description = "Hittable endpoints are welcome after admin login")
public class AdminController {
private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final JwtService jwtService;
	private final AdminService adminService;
	private final ResponseGenerator responseGenerator;
	
	@Operation(description = "Get End Point", summary = "This is a Token Validation and Get User by Id api", responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Unauthorized / Invalid token", responseCode = "401") })
	@GetMapping(value = "/getUser/{username}", produces = "application/json")
	public ResponseEntity<?>getUser(@PathVariable ("username") String username ,@RequestHeader("Authorization") String auth){
		
		 return  adminService.getadmindetials(username , auth);
	}
}
