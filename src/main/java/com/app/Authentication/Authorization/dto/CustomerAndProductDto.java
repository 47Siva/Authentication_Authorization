package com.app.Authentication.Authorization.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.app.Authentication.Authorization.entity.CustomerProduct;
import com.app.Authentication.Authorization.enumeration.GenderType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerAndProductDto {

	private UUID id;
	
	private String customerName;
	
	private UUID userId;
	
	private String email;
	
	private String mobileNo;
	
	private String address;
	
	private String date ;
	
	private GenderType gender;
	
	private List<CustomerProductDto> customerProducts;
}
