package com.app.Authentication.Authorization.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

		Optional<Product> productData = productRepository.findByName(request.getProductName());

		if (productData.isPresent()) {
			Product productObj = productData.get();
			productObj.setAvailableQuantity(productObj.getAvailableQuantity() + request.getProductQuantity());
			productObj.setPrice(request.getPrice());
			productObj = productRepository.saveAndFlush(productObj);
			
			ProductDto productDto = ProductDto.builder()
					.availableQuantity(productObj.getAvailableQuantity())
					.id(productObj.getId())
					.productName(productObj.getProductName())
					.price(productObj.getPrice()).build();
			return ResponseEntity.ok(productDto);
		} else {

			Product product = Product.builder().availableQuantity(request.getProductQuantity())
					.price(request.getPrice()).productName(request.getProductName()).build();
			
			if (request.getProductName().equals("string")) {
				throw new IllegalArgumentException("Product name not valid");
			} else {
				product = productRepository.save(product);
			}

			ProductDto dto = ProductDto.builder().availableQuantity(product.getAvailableQuantity()).id(product.getId())
					.price(product.getPrice()).productName(product.getProductName()).build();
			return ResponseEntity.ok(dto);
		}
	}

	public ResponseEntity<?> getProduct(UUID id) {
		Optional<Product> productData = productRepository.findById(id);
		if(productData.isPresent()) {
			Product productObj = productData.get();
			ProductDto productDto = ProductDto.builder()
					.availableQuantity(productObj.getAvailableQuantity())
					.id(productObj.getId())
					.productName(productObj.getProductName())
					.price(productObj.getPrice()).build();
			return ResponseEntity.ok(productDto);
		}else {
			return ResponseEntity.badRequest().body("Product Not Found ..!");
		}
	}

}
