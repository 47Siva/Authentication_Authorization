package com.app.Authentication.Authorization.Deserializer;

import java.util.ArrayList;
import java.util.List;

import com.app.Authentication.Authorization.advice.NullPointerException;
import com.app.Authentication.Authorization.entity.CustomerProduct;
import com.app.Authentication.Authorization.request.CustomerProductRequest;

public class CustomerProductConverter {
	public static List<CustomerProduct> convert(List<CustomerProductRequest> customerProductRequests) {

		 if (customerProductRequests == null) {
		        throw new NullPointerException("Product.request.required");
		    }

		    List<CustomerProduct> customerProducts = new ArrayList<>();
		    for (CustomerProductRequest request : customerProductRequests) {
		        CustomerProduct product = new CustomerProduct();
		        product.setProductName(request.getProductName());
		        product.setQuantity(request.getQuantity());
		        product.setPrice(request.getPrice());
		        product.setTotalAmount(request.getTotalAmount());
		        customerProducts.add(product);
		    }
		    return customerProducts;
	}
}
