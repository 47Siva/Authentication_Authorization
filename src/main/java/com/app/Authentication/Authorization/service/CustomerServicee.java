package com.app.Authentication.Authorization.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.Authentication.Authorization.dto.CustomerAndProductDto;
import com.app.Authentication.Authorization.dto.CustomerProductDto;
import com.app.Authentication.Authorization.entity.Customer;
import com.app.Authentication.Authorization.entity.CustomerProduct;
import com.app.Authentication.Authorization.entity.Product;
import com.app.Authentication.Authorization.repository.CustomerRepository;
import com.app.Authentication.Authorization.repository.ProductRepository;
import com.app.Authentication.Authorization.response.CustomerResponse;
import com.app.Authentication.Authorization.response.InvoiceResponse;
import com.app.Authentication.Authorization.response.MessageService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServicee {

	private final CustomerRepository customerRepository;
	private final ProductRepository productRepository;
	private @NonNull MessageService messagePropertySource;

	public Optional<Customer> findByDuplicateEmail(String email) {
		return customerRepository.findByDuplicateEamil(email);
	}

	public Optional<Customer> getMobileNos(String mobileNo) {
		return customerRepository.findByDuplicateNumber(mobileNo);
	}

	public ResponseEntity<?> getAllCustomer() {

		List<Customer> customerData = customerRepository.findAll();
		ArrayList<CustomerResponse> customerResponses = new ArrayList<>();
		Map<String, Object> response = new HashMap<>();

		if (customerData.isEmpty()) {
			response.put("Error", messagePropertySource.messageResponse("Customers.empty"));
			response.put("Status", HttpStatus.NO_CONTENT);
			response.put("StatusCode", HttpStatus.NO_CONTENT.toString());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
		} else {
			for (Customer customerObj : customerData) {
				CustomerResponse customer = CustomerResponse.builder().id(customerObj.getId())
						.customerName(customerObj.getCustomerName()).date(customerObj.getDate())
						.address(customerObj.getAddress()).email(customerObj.getEmail()).gender(customerObj.getGender())
						.mobileNo(customerObj.getMobileNo()).build();
				customerResponses.add(customer);
			}
			response.put("Data", customerResponses);
			response.put("Status", HttpStatus.OK);
			response.put("StatusCode", HttpStatus.OK.toString());
			return ResponseEntity.ok(response);
		}
	}

	public ResponseEntity<?> getAllCustomerAndProduct() {
		List<Customer> customerData = customerRepository.findAll();
		List<CustomerAndProductDto> customerDtoList = new ArrayList<CustomerAndProductDto>();
		Map<String, Object> response = new HashMap<>();
		if (customerData.isEmpty()) {
			response.put("Error", messagePropertySource.messageResponse("Customers.empty"));
			response.put("Status", HttpStatus.NO_CONTENT);
			response.put("StatusCode", HttpStatus.NO_CONTENT.toString());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
		} else {

			for (Customer customerObj : customerData) {
				CustomerAndProductDto dto = CustomerAndProductDto.builder().customerName(customerObj.getCustomerName())
						.date(customerObj.getDate()).email(customerObj.getEmail()).gender(customerObj.getGender())
						.id(customerObj.getId()).mobileNo(customerObj.getMobileNo()).address(customerObj.getAddress())
						.build();
				List<CustomerProductDto> customerProductList = new ArrayList<>();
				for (CustomerProduct customerProductObj : customerObj.getCustomerProducet()) {
					CustomerProductDto dto2 = CustomerProductDto.builder()
							.id(customerProductObj.getId()).price(customerProductObj.getPrice())
							.productName(customerProductObj.getProductName())
							.quantity(customerProductObj.getQuantity()).totalAmount(customerProductObj.getTotalAmount())
							.build();
					customerProductList.add(dto2);
				}
				dto.setCustomerProducts(customerProductList);
				customerDtoList.add(dto);
			}
			return ResponseEntity.ok(customerDtoList);
		}
	}

	public ResponseEntity<?> getCustomer(UUID id) {
		Optional<Customer> customer = customerRepository.findById(id);
		Map<String, Object> response = new HashMap<>();
		if (customer.isPresent()) {
			Customer customer2 = customer.get();

			CustomerAndProductDto customerAndProductDto = CustomerAndProductDto.builder().id(customer2.getId())
					.address(customer2.getAddress()).customerName(customer2.getCustomerName()).date(customer2.getDate())
					.email(customer2.getEmail()).gender(customer2.getGender()).mobileNo(customer2.getMobileNo())
					.customerProducts(null).build();

			double totalProductAmount = 0d;
			double singleProductAmount = 0d;
			int availableQuantity = 0;

			ArrayList<CustomerProductDto> dtolist = new ArrayList<>();
			List<CustomerProduct> customerProduct = customer2.getCustomerProducet();
			for (CustomerProduct obj : customerProduct) {
				CustomerProductDto customerProductDto = CustomerProductDto.builder().id(obj.getId())
						.price(obj.getPrice()).productName(obj.getProductName()).quantity(obj.getQuantity())
						.totalAmount(obj.getTotalAmount()).build();
				Optional<Product> product1 = productRepository.findByName(obj.getProductName());
				Product productobj = product1.get();
				singleProductAmount = obj.getQuantity() * productobj.getPrice();
				totalProductAmount += singleProductAmount;
				dtolist.add(customerProductDto);
			}
			customerAndProductDto.setCustomerProducts(dtolist);

			String Gststr = "5%";
			String shopDiscountStr = "2%";
			double shopDiscount = Double.parseDouble(shopDiscountStr.replace("%", "")) / 100.0;
			double Gst = Double.parseDouble(Gststr.replace("%", "")) / 100.0;
			double GstAmount = Gst * totalProductAmount;
			double discountAmount = shopDiscount * totalProductAmount;
			double grandTotal = totalProductAmount + GstAmount - discountAmount;

			InvoiceResponse invoiceResponse = InvoiceResponse.builder().customer(customerAndProductDto).gst(Gststr)
					.shopDiscount(shopDiscountStr).gstAmount(GstAmount).discountAmount(discountAmount)
					.grandTotalAmount(grandTotal).totalprouctsAmount(totalProductAmount).build();

			response.put("Data", invoiceResponse);
			response.put("Status", HttpStatus.OK);
			response.put("StatusCode", HttpStatus.OK.toString());
			return ResponseEntity.ok(response);
		} else {
			response.put("Error", messagePropertySource.messageResponse("Customer.notfound"));
			response.put("Status", HttpStatus.NO_CONTENT);
			response.put("StatusCode", HttpStatus.NO_CONTENT.toString());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
		}
	}

}
