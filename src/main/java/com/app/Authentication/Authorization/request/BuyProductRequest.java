package com.app.Authentication.Authorization.request;

import java.util.List;
import java.util.UUID;

import com.app.Authentication.Authorization.enumeration.GenderType;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class BuyProductRequest {
	
	private String userId;
	
	private String customerName;

	private String email;

	private String mobileNo;

	private String address;

	private GenderType gender;

	private List<CustomerProductRequest> customerProduct;
}
