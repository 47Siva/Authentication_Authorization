package com.app.Authentication.Authorization.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRequest {

	private String productName;
	
	private int productQuantity;
	
	private long price;
}
