package com.app.Authentication.Authorization.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerProductDto {

	private UUID id;
	
	private String productName;
	
	private int quantity;
	
	private double price;
	
	private double totalAmount;
}
