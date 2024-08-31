package com.app.Authentication.Authorization.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerProductDto {
	
	private String productName;
	
	private String date;
	
	private int quantity;
	
	private double price;
	
	private double totalAmount;
}
