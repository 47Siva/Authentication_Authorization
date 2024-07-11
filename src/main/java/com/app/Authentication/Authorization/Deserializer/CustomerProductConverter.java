package com.app.Authentication.Authorization.Deserializer;

import java.util.List;
import java.util.stream.Collectors;

import com.app.Authentication.Authorization.advice.NullPointerException;
import com.app.Authentication.Authorization.entity.CustomerProduct;
import com.app.Authentication.Authorization.request.CustomerProductRequest;

public class CustomerProductConverter {
	public static List<CustomerProduct> convert(List<CustomerProductRequest> customerProductRequests) {

		if (customerProductRequests != null) {
			return customerProductRequests.stream().map(request -> {
				CustomerProduct product = new CustomerProduct();
				product.setProductName(request.getProductName());
				product.setQuantity(request.getQuantity());
				product.setPrice(request.getPrice());
				product.setTotalAmount(request.getTotalAmount());
				return product;
			}).collect(Collectors.toList());
		}else {
			throw new NullPointerException("Product.request.required");
		}
	}
}
