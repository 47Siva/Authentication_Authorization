package com.app.Authentication.Authorization.dto;

import java.util.UUID;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {

	private UUID id;

	private String productName;

	private int availableQuantity;

	private long price;
}
