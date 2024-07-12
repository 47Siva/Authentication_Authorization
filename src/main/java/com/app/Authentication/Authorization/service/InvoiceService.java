package com.app.Authentication.Authorization.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.Authentication.Authorization.advice.NullPointerException;
import com.app.Authentication.Authorization.dto.CustomerDto;
import com.app.Authentication.Authorization.dto.CustomerProductDto;
import com.app.Authentication.Authorization.entity.Customer;
import com.app.Authentication.Authorization.entity.CustomerProduct;
import com.app.Authentication.Authorization.entity.Product;
import com.app.Authentication.Authorization.repository.CustomerProductRepository;
import com.app.Authentication.Authorization.repository.CustomerRepository;
import com.app.Authentication.Authorization.repository.ProductRepository;
import com.app.Authentication.Authorization.request.BuyProductRequest;
import com.app.Authentication.Authorization.request.CustomerProductRequest;
import com.app.Authentication.Authorization.response.InvoiceResponse;
import com.app.Authentication.Authorization.response.MessageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceService {

	private final MessageService messageSource;
	private final ProductRepository productRepository;
	private final CustomerProductRepository customerProductRepository;
	private final CustomerRepository customerRepository;

	public ResponseEntity<?> buyProduct(Customer customer, BuyProductRequest request) {

		if (customer == null) {
			throw new IllegalArgumentException(messageSource.messageResponse("customer.required"));
		}

		Customer customerEntity = Customer.builder().customerName(customer.getCustomerName())
				.address(customer.getAddress()).date(customer.getDate()).email(customer.getEmail())
				.gender(customer.getGender()).mobileNo(customer.getMobileNo()).build();

		double totalProductAmount = 0d;
		double singleProductAmount = 0d;
		int availableQuantity = 0;

		List<CustomerProduct> productlist = new ArrayList<>();
		for (CustomerProductRequest product : request.getCustomerProduct()) {
			Optional<Product> product1 = productRepository.findByName(product.getProductName());

			if (product1.isPresent()) {
				Product productobj = product1.get();

				if (product.getQuantity() <= 0) {
					String error = messageSource.messageResponse("product.quantity.required");
					throw new NullPointerException(error);
				}

				if (productobj.getAvailableQuantity() < product.getQuantity()) {
					String error = messageSource.messageResponse("product.quantity.notvalid");
					throw new NullPointerException(error);
//					singleProductAmount = productobj.getPrice() * productobj.getAvailableQuantity();
//					product.setQuantity(productobj.getAvailableQuantity());
				} else {
					singleProductAmount = product.getQuantity() * productobj.getPrice();
					availableQuantity = productobj.getAvailableQuantity() - product.getQuantity();
				}

				totalProductAmount = totalProductAmount + singleProductAmount;
				productobj.setAvailableQuantity(availableQuantity);
				productRepository.save(productobj);
				availableQuantity = 0;

				boolean productExists = false;
				for (CustomerProduct customerProduct : productlist) {
					if (customerProduct.getProductName().equals(productobj.getProductName())) {
						customerProduct.setQuantity(customerProduct.getQuantity() + product.getQuantity());
						customerProduct.setTotalAmount(customerProduct.getTotalAmount() + singleProductAmount);
						productExists = true;
						break;
					}
				}

				if (!productExists) {
					CustomerProduct customerProduct = CustomerProduct.builder().price(productobj.getPrice())
							.productName(productobj.getProductName()).quantity(product.getQuantity())
							.totalAmount(singleProductAmount).build();
					productlist.add(customerProduct);
				}
			} else {
				String error = messageSource.messageResponse("product.not.valid");
				throw new NullPointerException(error);
			}
		}
		customerEntity.setCustomerProducet(productlist);
		Customer customerData = customerRepository.save(customerEntity);

		String Gststr = "5%";
		String shopDiscountStr = "2%";
		double shopDiscount = Double.parseDouble(shopDiscountStr.replace("%", "")) / 100.0;
		double Gst = Double.parseDouble(Gststr.replace("%", "")) / 100.0;
		double GstAmount = Gst * totalProductAmount;
		double discountAmount = shopDiscount * totalProductAmount;
		double grandTotal = totalProductAmount + GstAmount;
		grandTotal = totalProductAmount - discountAmount;

		CustomerDto customerdto = CustomerDto.builder().customerName(customerData.getCustomerName())
				.date(LocalDate.parse(customerData.getDate())).email(customerData.getEmail())
				.address(customerData.getAddress()).gender(customerData.getGender()).id(customerData.getId())
				.mobileNo(customerData.getMobileNo()).build();
		List<CustomerProductDto> productDto = new ArrayList<>();
		for (CustomerProduct dto2 : customerData.getCustomerProducet()) {
			CustomerProductDto customerProductDto = CustomerProductDto.builder().id(dto2.getId()).price(dto2.getPrice())
					.productName(dto2.getProductName()).quantity(dto2.getQuantity()).totalAmount(dto2.getTotalAmount())
					.build();
			productDto.add(customerProductDto);
		}
		customerdto.setCustomerProducts(productDto);

		InvoiceResponse invoiceResponse = InvoiceResponse.builder().customer(customerdto).gst(Gststr).shopDiscount(shopDiscountStr)
				.gstAmount(GstAmount).discountAmount(discountAmount).grandTotalAmount(grandTotal).totalprouctsAmount(totalProductAmount)
				.build();

		return ResponseEntity.ok(invoiceResponse);
	}

}
