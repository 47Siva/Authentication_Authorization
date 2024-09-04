package com.app.Authentication.Authorization.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class CustomerProductRequest {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyy")
	private String date;
	
	private String productName;
	
	private int quantity;
	
	private double price;
	
	private double totalAmount;
}
