package com.app.Authentication.Authorization.service;

import java.util.Optional;

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
			Product productobj = productData.get();
			productobj.setAvailableQuantity(request.getProductQuantity());
			productobj.setPrice(request.getPrice());
			productobj = productRepository.saveAndFlush(productobj);
			return ResponseEntity.ok(productobj);
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

}
