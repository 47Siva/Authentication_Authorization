package com.app.Authentication.Authorization.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.Authentication.Authorization.advice.NullPointerException;
import com.app.Authentication.Authorization.dto.CustomerAndProductDto;
import com.app.Authentication.Authorization.dto.CustomerProductDto;
import com.app.Authentication.Authorization.entity.Customer;
import com.app.Authentication.Authorization.entity.CustomerProduct;
import com.app.Authentication.Authorization.entity.Product;
import com.app.Authentication.Authorization.enumeration.RequestType;
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

	public ResponseEntity<?> buyProduct(Customer customer, BuyProductRequest request, RequestType requestType,
			UUID id) {

		Customer customerEntity = null;
		if (RequestType.PUT.equals(requestType)) {
			Optional<Customer> customerData = customerRepository.findById(id);
			if (customerData.isPresent()) {
				customerEntity = customerData.get();
				// Update the existing customer's fields with the new values
				customerEntity.setCustomerName(customer.getCustomerName());
				customerEntity.setAddress(customer.getAddress());
				customerEntity.setDate(customer.getDate());
				customerEntity.setEmail(customer.getEmail());
				customerEntity.setGender(customer.getGender());
				customerEntity.setMobileNo(customer.getMobileNo());
			}
		}
		if (RequestType.POST.equals(requestType)) {
			// Create a new Customer entity
			customerEntity = Customer.builder().customerName(customer.getCustomerName()).address(customer.getAddress())
					.date(customer.getDate()).email(customer.getEmail()).gender(customer.getGender())
					.mobileNo(customer.getMobileNo()).build();
		}

		double totalProductAmount = 0d;
		double singleProductAmount = 0d;
		int availableQuantity = 0;

		List<CustomerProduct> productlist = customerEntity.getCustomerProducet() != null
				? new ArrayList<>(customerEntity.getCustomerProducet())
				: new ArrayList<>();
		List<CustomerProduct> currentProductList = new ArrayList<>();
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

				// Check if the product already exists in the customer's list
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
					// Add the new product to the customer's list
					CustomerProduct customerProduct = CustomerProduct.builder().price(productobj.getPrice())
							.productName(productobj.getProductName()).quantity(product.getQuantity())
							.totalAmount(singleProductAmount).build();
					productlist.add(customerProduct);
					currentProductList.add(customerProduct);
				}else {
					// Add the updated product to the current transaction list
					for (CustomerProduct existingProduct : productlist) {
						if (existingProduct.getProductName().equals(productobj.getProductName())) {
							currentProductList.add(existingProduct);
							break;
						}
					}
				}
			} else {
				String error = messageSource.messageResponse("product.not.valid");
				throw new NullPointerException(error);
			}
		}
		
		// Update the customer's product list
		customerEntity.setCustomerProducet(productlist);
		Customer customerData = customerRepository.save(customerEntity);

		// Calculate GST, discount, and grand total
		String Gststr = "5%";
		String shopDiscountStr = "2%";
		double shopDiscount = Double.parseDouble(shopDiscountStr.replace("%", "")) / 100.0;
		double Gst = Double.parseDouble(Gststr.replace("%", "")) / 100.0;
		double GstAmount = Gst * totalProductAmount;
		double discountAmount = shopDiscount * totalProductAmount;
		double grandTotal = totalProductAmount + GstAmount - discountAmount;

	    // Prepare the response DTO
		CustomerAndProductDto customerdto = CustomerAndProductDto.builder().customerName(customerData.getCustomerName())
				.date(customerData.getDate()).email(customerData.getEmail()).address(customerData.getAddress())
				.gender(customerData.getGender()).id(customerData.getId()).mobileNo(customerData.getMobileNo()).build();
		List<CustomerProductDto> productDto = new ArrayList<>();
		for (CustomerProduct currentProduct : currentProductList) {
			CustomerProductDto customerProductDto = CustomerProductDto.builder()
					.price(currentProduct.getPrice())
					.productName(currentProduct.getProductName())
					.quantity(currentProduct.getQuantity())
					.totalAmount(currentProduct.getTotalAmount())
					.build();
			productDto.add(customerProductDto);
		}
		customerdto.setCustomerProducts(productDto);


	    // Build the invoice response
		InvoiceResponse invoiceResponse = InvoiceResponse.builder().customer(customerdto).gst(Gststr)
				.shopDiscount(shopDiscountStr).gstAmount(GstAmount).discountAmount(discountAmount)
				.grandTotalAmount(grandTotal).totalprouctsAmount(totalProductAmount).build();

		return ResponseEntity.ok(invoiceResponse);
	}

}
