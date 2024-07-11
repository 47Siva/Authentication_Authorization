package com.app.Authentication.Authorization.request;

import lombok.Data;

@Data
public class CustomerProductRequest {

	private String productName;
	
	private int quantity;
	
	private double price;
	
	private double totalAmount;
}
