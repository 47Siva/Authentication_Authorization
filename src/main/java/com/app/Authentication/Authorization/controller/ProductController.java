package com.app.Authentication.Authorization.controller;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.app.Authentication.Authorization.request.ProductRequest;
import com.app.Authentication.Authorization.response.ResponseGenerator;
import com.app.Authentication.Authorization.response.TransactionContext;
import com.app.Authentication.Authorization.service.ProductService;

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
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Product request payload") 
			@RequestHeader HttpHeaders httpHeaders,
			@RequestBody ProductRequest request) {
		ResponseEntity<?> productservice = productService.addProducts(request);
		
		TransactionContext context = responseGenerator.generateTransationContext(httpHeaders);
		try {
			return responseGenerator.successResponse(context, productservice.getBody(), HttpStatus.OK,false);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@Operation(description = "Post End Point", summary = "Add Product List Api", responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Unauthorized / Invalid token", responseCode = "401") })
	@PostMapping("/add/product/list")
	public ResponseEntity<?> addProductsList(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Product request payload") 
			@RequestHeader HttpHeaders httpHeaders,
			@RequestBody List<ProductRequest> request) {
		ResponseEntity<?> productservice = productService.addProductList(request);
		
		TransactionContext context = responseGenerator.generateTransationContext(httpHeaders);
		try {
			return responseGenerator.successResponse(context, productservice.getBody(), HttpStatus.OK,false);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@Operation(description = "Post End Point", summary = "Get Product Api", responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Unauthorized / Invalid token", responseCode = "401") })
	@GetMapping("/get/product/{productName}")
	public ResponseEntity<?> getProduct(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Give the Product ID ") 
			@RequestHeader HttpHeaders httpHeaders,
			@PathVariable String productName){
		TransactionContext context = responseGenerator.generateTransationContext(httpHeaders);
		try {
			ResponseEntity<?>productservice =productService.getProduct(productName);
			return responseGenerator.successResponse(context, productservice.getBody(), HttpStatus.OK,false);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@Operation(description = "Post End Point", summary = "Get All Product Api", responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Unauthorized / Invalid token", responseCode = "401") })
	@GetMapping("/get/All/product")
	public ResponseEntity<?> getAllProduct(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Give the Product ID ") 
			@RequestHeader HttpHeaders httpHeaders){
		TransactionContext context = responseGenerator.generateTransationContext(httpHeaders);
		try {
			ResponseEntity<?>productservice =productService.getAllProduct();
			return responseGenerator.successResponse(context, productservice.getBody(), HttpStatus.OK,false);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
