package com.app.Authentication.Authorization.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.Authentication.Authorization.dto.ProductDto;
import com.app.Authentication.Authorization.entity.Product;
import com.app.Authentication.Authorization.repository.ProductRepository;
import com.app.Authentication.Authorization.request.ProductRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	public ResponseEntity<?> addProducts(ProductRequest request) {
		
		Product product = Product.builder().availableQuantity(request.getProductQuantity())
				.price(request.getPrice()).productName(request.getProductName())
				.build();
		
	    product = productRepository.save(product);
	    
	    ProductDto dto = ProductDto.builder().availableQuantity(product.getAvailableQuantity())
	    		.id(product.getId()).price(product.getPrice()).productName(product.getProductName()).build();
		return ResponseEntity.ok(dto);
	}
	
	
}
