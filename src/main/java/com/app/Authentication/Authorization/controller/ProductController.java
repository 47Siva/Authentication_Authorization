package com.app.Authentication.Authorization.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Authentication.Authorization.entity.Product;
import com.app.Authentication.Authorization.request.BuyProductRequest;
import com.app.Authentication.Authorization.request.ProductRequest;
import com.app.Authentication.Authorization.response.ResponseGenerator;
import com.app.Authentication.Authorization.response.TransactionContext;
import com.app.Authentication.Authorization.service.InvoiceService;
import com.app.Authentication.Authorization.service.ProductService;
import com.app.Authentication.Authorization.validator.CustomerValidator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/product")
@Tag(name = "Product_Controller", description = "API for managing Product's")
public class ProductController {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final ProductService productService;
	private final ResponseGenerator responseGenerator;

	@Operation(description = "Post End Point", summary = "Add Product Api", responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Unauthorized / Invalid token", responseCode = "401") })
	@PostMapping("/add/product")
	public ResponseEntity<?> addProducts(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Product request payload") @RequestHeader HttpHeaders httpHeaders,
			@RequestBody ProductRequest request) {
		ResponseEntity<?> productservice = productService.addProducts(request);
		
		TransactionContext context = responseGenerator.generateTransationContext(httpHeaders);
		try {
			return responseGenerator.successResponse(context, productservice, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
