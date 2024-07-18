package com.app.Authentication.Authorization.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Authentication.Authorization.advice.NullPointerException;
import com.app.Authentication.Authorization.entity.Customer;
import com.app.Authentication.Authorization.enumeration.RequestType;
import com.app.Authentication.Authorization.request.BuyProductRequest;
import com.app.Authentication.Authorization.response.MessageService;
import com.app.Authentication.Authorization.response.ResponseGenerator;
import com.app.Authentication.Authorization.response.TransactionContext;
import com.app.Authentication.Authorization.service.InvoiceService;
import com.app.Authentication.Authorization.service.UserService;
import com.app.Authentication.Authorization.validator.CustomerValidator;
import com.app.Authentication.Authorization.validator.ValidationResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/invoice")
@Tag(name = "Invoice_Controller", description = "API for managing invoices, including creating, retrieving, updating, and deleting invoice records")
public class InvoiceController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final UserService userService;
	private final MessageService messageService;
	private final ResponseGenerator responseGenerator;
	private final InvoiceService invoiceService;
	private final CustomerValidator customerValidator;

	@Operation(description = "Post End Point", summary = "Customer buy new product", responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Unauthorized / Invalid token", responseCode = "401") })
	@PostMapping("/buy/product")
	public ResponseEntity<?> buyProducts(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Signup request payload") @RequestHeader HttpHeaders httpHeaders,
			@RequestBody BuyProductRequest request) {

		ValidationResult validationResult = customerValidator.validate(RequestType.POST, request);
		Customer customer = (Customer) validationResult.getObject();

		ResponseEntity<?> response = invoiceService.buyProduct(customer, request);

		TransactionContext context = responseGenerator.generateTransationContext(httpHeaders);
		try {
			return responseGenerator.successResponse(context, response.getBody(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	@Operation(description = "Get End Point", summary = "This is a get All Customer Api", responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Unauthorized / Invalid token", responseCode = "401") })
	@GetMapping("/getAllCustomer")
	public ResponseEntity<?> getAllCustomer(@RequestHeader HttpHeaders httpHeaders) {
		
		ResponseEntity<?>response = null;
		
//		try {
		response = userService.getAllCustomer();
//		}catch (java.lang.NullPointerException e) {
//			String error = messageService.messageResponse("Customers.empty");
//			throw new NullPointerException(error);
//		}
		TransactionContext context = responseGenerator.generateTransationContext(httpHeaders);
		try {
			return responseGenerator.successResponse(context, response.getBody(), HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@Operation(description = "Get End Point", summary = "This is a get specific Customer Api", responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Unauthorized / Invalid token", responseCode = "401") })
	@GetMapping("/getCustomer/{id}")
	public ResponseEntity<?> getCustomer(@RequestHeader HttpHeaders httpHeaders,
			@PathVariable UUID id) {
		
		ResponseEntity<?> response = userService.getCustomer(id);
		
		TransactionContext context = responseGenerator.generateTransationContext(httpHeaders);
		try {
			return responseGenerator.successResponse(context, response.getBody(), HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
