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

import com.app.Authentication.Authorization.entity.City;
import com.app.Authentication.Authorization.entity.Country;
import com.app.Authentication.Authorization.entity.State;
import com.app.Authentication.Authorization.enumeration.RequestType;
import com.app.Authentication.Authorization.request.AddressRequest;
import com.app.Authentication.Authorization.request.AddressRequest.CityRequest;
import com.app.Authentication.Authorization.response.MessageService;
import com.app.Authentication.Authorization.response.ResponseGenerator;
import com.app.Authentication.Authorization.response.TransactionContext;
import com.app.Authentication.Authorization.service.AddressService;
import com.app.Authentication.Authorization.validator.AddressValidator;
import com.app.Authentication.Authorization.validator.ValidationResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/address")
@Tag(name = "Address_Controller", description = "Endpoints for managing addresses, "
		                                      + "including adding and retrieving countries, states, and cities.")
public class AddressControler {

private final Logger logger = LoggerFactory.getLogger(getClass());
	
//	private final JwtService jwtService;
	private final ResponseGenerator responseGenerator;
	private @NonNull MessageService messagePropertySource;
	private final AddressValidator addressValidator;
	private final AddressService addressService;
	
	@Operation(
		    description = "PUT End Point",
		    summary = "Allows to update existing address details (state or city).",
		    responses = {
		        @ApiResponse(description = "Success", responseCode = "200"),
		        @ApiResponse(description = "Unauthorized / Invalid token", responseCode = "401"),
		        @ApiResponse(description = "Bad Request / Validation Error", responseCode = "400")
		    }
		)
		@PostMapping(value = "/addCountry")
		public ResponseEntity<?> addCountry(
		    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Update request payload for country")
		    @RequestBody AddressRequest.CountryRequest request,
		    @RequestHeader HttpHeaders httpHeader
//		    @RequestHeader("Authorization") String auth
		) throws Exception {
		    // Generate a transaction context for tracking
		    TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		    // Validate the incoming request payload
		    ValidationResult validationResult = addressValidator.validateCountry(request);
		    Country country = (Country) validationResult.getObject();

		    // Save or update the validated address object'
		    addressService.saveOrUpdate(country);

		    try {
		        return responseGenerator.successResponse(
		            context,
		            messagePropertySource.messageResponse("country.add.success"),
		            HttpStatus.OK,
		            false
		        );
		    } catch (Exception e) {
		        e.printStackTrace();
		        logger.error(e.getMessage(), e);
		        return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		    }
		}
	
	@Operation(
		    description = "POST End Point for adding a new state",
		    summary = "Allows adding a new state under an existing country.",
		    responses = {
		        @ApiResponse(description = "State added successfully", responseCode = "200"),
		        @ApiResponse(description = "Unauthorized / Invalid token", responseCode = "401"),
		        @ApiResponse(description = "Invalid input or conflict", responseCode = "400")
		    }
		)
		@PostMapping(value = "/addState", consumes = "application/json", produces = "application/json")
		public ResponseEntity<?> addState(
		        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Add State request payload") 
		        @RequestBody AddressRequest.StateRequest request,
		        @RequestHeader HttpHeaders httpHeaders
//		        @RequestHeader("Authorization") String auth
		        ) throws Exception {

		    TransactionContext context = responseGenerator.generateTransationContext(httpHeaders);

		    // Validate request payload
		    ValidationResult validationResult = addressValidator.validateState(request);

		    // Save state
		    addressService.addState((State) validationResult.getObject());

		    try {
		        return responseGenerator.successResponse(
		            context,
		            messagePropertySource.messageResponse("state.add.success"),
		            HttpStatus.OK,
		            false
		        );
		    } catch (Exception e) {
		        e.printStackTrace();
		        logger.error(e.getMessage(), e);
		        return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		    }
		}
	
	@Operation(
		    description = "POST End Point for adding a new city",
		    summary = "Allows adding a new city under an existing state.",
		    responses = {
		        @ApiResponse(description = "City added successfully", responseCode = "200"),
		        @ApiResponse(description = "Unauthorized / Invalid token", responseCode = "401"),
		        @ApiResponse(description = "Invalid input or conflict", responseCode = "400")
		    }
		)
		@PostMapping(value = "/addCity", consumes = "application/json", produces = "application/json")
		public ResponseEntity<?> addCity(
		        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Add City request payload") 
		        @RequestBody AddressRequest.CityRequest request,
		        @RequestHeader HttpHeaders httpHeaders
//		        @RequestHeader("Authorization") String auth
		        ) throws Exception {

		    TransactionContext context = responseGenerator.generateTransationContext(httpHeaders);

		    // Validate request payload
		    ValidationResult validationResult = addressValidator.validateCity(request);

		    // Save city
		    addressService.addCity((City) validationResult.getObject());

		    try {
		        return responseGenerator.successResponse(
		            context,
		            messagePropertySource.messageResponse("city.add.success"),
		            HttpStatus.OK,
		            false
		        );
		    } catch (Exception e) {
		        e.printStackTrace();
		        logger.error(e.getMessage(), e);
		        return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		    }
		}



}
