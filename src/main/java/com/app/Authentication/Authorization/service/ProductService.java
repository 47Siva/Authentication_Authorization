package com.app.Authentication.Authorization.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.Authentication.Authorization.advice.NullPointerException;
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

			ProductDto productDto = ProductDto.builder().availableQuantity(productObj.getAvailableQuantity())
					.id(productObj.getId()).productName(productObj.getProductName()).price(productObj.getPrice())
					.build();
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

	public ResponseEntity<?> addProductList(List<ProductRequest> productList) {
		Map<String, ProductDto> productDtoMap = new HashMap<>();
		for (ProductRequest productOptional : productList) {
			Optional<Product> productData = productRepository.findByName(productOptional.getProductName());
			Product productObj;

			if (productData.isPresent()) {
				productObj = productData.get();
				productObj
						.setAvailableQuantity(productObj.getAvailableQuantity() + productOptional.getProductQuantity());
				productObj.setPrice(productOptional.getPrice());
				productObj = productRepository.saveAndFlush(productObj);
			} else {
				productObj = Product.builder().availableQuantity(productOptional.getProductQuantity())
						.price(productOptional.getPrice()).productName(productOptional.getProductName()).build();
				if (productOptional.getProductName().equals("string")) {
					throw new IllegalArgumentException("Product name not valid");
				} else {
					productObj = productRepository.save(productObj);
				}
			}

			ProductDto dto = productDtoMap.get(productObj.getProductName());
			if (dto == null) {
				dto = ProductDto.builder().availableQuantity(productObj.getAvailableQuantity()).id(productObj.getId())
						.price(productObj.getPrice()).productName(productObj.getProductName()).build();
				productDtoMap.put(productObj.getProductName(), dto);
			} else {
				dto.setAvailableQuantity(dto.getAvailableQuantity() + productOptional.getProductQuantity());
				dto.setPrice(productOptional.getPrice());
			}
		}
		return ResponseEntity.ok(new ArrayList<>(productDtoMap.values()));
	}

	public ResponseEntity<?> getAllProduct() {
		List<Product> productDataList = productRepository.findAll();

		if (productDataList.isEmpty()) {
			throw new NullPointerException("Product List is Empty");
		} else {
			List<ProductDto> productList = new ArrayList<>();
			for (Product productObj : productDataList) {
				ProductDto customer = ProductDto.builder().id(productObj.getId())
						.availableQuantity(productObj.getAvailableQuantity()).price(productObj.getPrice())
						.productName(productObj.getProductName()).build();
				productList.add(customer);
			}
			return ResponseEntity.ok(productList);
		}
	}

	public ResponseEntity<?> getProduct(String productName) {
		Optional<Product> productData = productRepository.findByName(productName);
		if (productData.isPresent()) {
			Product productObj = productData.get();
			ProductDto productDto = ProductDto.builder().availableQuantity(productObj.getAvailableQuantity())
					.id(productObj.getId()).productName(productObj.getProductName()).price(productObj.getPrice())
					.build();
			return ResponseEntity.ok(productDto);
		} else {
			return ResponseEntity.badRequest().body("Product Not Found ..!");
		}
	}

}
