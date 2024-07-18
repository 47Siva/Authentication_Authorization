package com.app.Authentication.Authorization.service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.Authentication.Authorization.advice.SignatureException;
import com.app.Authentication.Authorization.dto.CustomerAndProductDto;
import com.app.Authentication.Authorization.dto.CustomerProductDto;
import com.app.Authentication.Authorization.entity.Customer;
import com.app.Authentication.Authorization.entity.CustomerProduct;
import com.app.Authentication.Authorization.entity.Product;
import com.app.Authentication.Authorization.entity.User;
import com.app.Authentication.Authorization.enumeration.Role;
import com.app.Authentication.Authorization.enumeration.Status;
import com.app.Authentication.Authorization.repository.CustomerRepository;
import com.app.Authentication.Authorization.repository.ProductRepository;
import com.app.Authentication.Authorization.repository.UserRepository;
import com.app.Authentication.Authorization.response.CustomerResponse;
import com.app.Authentication.Authorization.response.InvoiceResponse;
import com.app.Authentication.Authorization.response.MessageService;
import com.app.Authentication.Authorization.response.UserMapper;
import com.app.Authentication.Authorization.response.UserResponse;
import com.app.Authentication.Authorization.security.JwtService;
import com.app.Authentication.Authorization.util.PasswordUtil;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;
	private final CustomerRepository customerRepository;
	private final JwtService jwtService;
	private final ProductRepository productRepository;
	private @NonNull MessageService messagePropertySource;

	public User createUser(User user) {
		if (user == null) {
			throw new IllegalArgumentException("User cannot be null");
		}
		if (user.getUserRole() == null) {
			throw new IllegalArgumentException("Role cannot be null");
		}
		var userobj = User.builder().email(user.getEmail()).mobileNo(user.getMobileNo())
				.password(PasswordUtil.getEncryptedPassword(user.getPassword())).userRole(user.getUserRole())
				.status(Status.ACTIVE).userName(user.getUsername()).build();
		return userRepository.saveAndFlush(userobj);
	}

	public Optional<User> findByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}

	public Optional<User> findByDuplicateEmail(String email) {

		return userRepository.findByDuplicateEamil(email);
	}

	public Optional<User> findByDuplicateUserName(String userName) {
		return userRepository.findByDuplicateUserName(userName);
	}

	public Optional<User> getMobileNos(String phoneNumber) {
		return userRepository.findByDuplicateNumber(phoneNumber);
	}

	public Optional<User> findByDuplicatePassword(String password) {

		String enpassword = PasswordUtil.getEncryptedPassword(password);

		return userRepository.findByDuplicatePassword(enpassword);
	}

	public Optional<User> findByUserId(UUID id) {
		return userRepository.findById(id);
	}

	public void saveOrUpdate(User userObject) {
		userRepository.saveAndFlush(userObject);
	}

	public Optional<User> getByMobileNo(String mobileNo) {
		return userRepository.getByMobileNo(mobileNo);
	}

	public Optional<User> findByUserEmail(String email) {
		return userRepository.findByUserEmail(email);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOptional = userRepository.findByUserName(username);
		if (!userOptional.isPresent()) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		User user = userOptional.get();
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				user.getAuthorities());
	}

	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllUsers(String auth) throws SignatureException {
		Map<String, Object> response = new HashMap<>();
		String token = jwtService.extractToken(auth);

		// Validate token
		if (!jwtService.validateToken(token)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
		}

		String tokenRole = jwtService.extractRole(token);
		if (!"ADMIN".equals(tokenRole)) {
			response.put("Status", HttpStatus.FORBIDDEN.toString());
			response.put("message", "Access denied");
			response.put("Error", HttpStatus.FORBIDDEN);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}

		List<User> users = userRepository.findAll();
		List<UserResponse> userResponses = users.stream().map(UserMapper::toUserResponse).collect(Collectors.toList());
		return ResponseEntity.ok(userResponses);
	}

	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getuserDetailsUserNameFromToken(String useremail, String auth) throws SignatureException {

		// Construct response
		Map<String, Object> response = new HashMap<>();
		String token = jwtService.extractToken(auth);
		String tokenName = jwtService.extractUserName(token);

		// Validate token
		if (!jwtService.validateToken(token)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
		}

		// Optional<User> userdetails1 = userRepository.findByUserName(useremail);
		Optional<User> userdetails2 = userRepository.findByUserName(tokenName);
		if (useremail.equals(userdetails2.get().getEmail())) {
			if (userdetails2.isPresent()) {
				User user = userdetails2.get();

				if (!user.getUserRole().equals(Role.USER)) {
					response.put("Status", HttpStatus.NOT_ACCEPTABLE.toString());
					response.put("message", "User can't access the request");
					response.put("Error", HttpStatus.NOT_ACCEPTABLE);
					return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
				}
				UserResponse userresponse = UserResponse.builder().userId(user.getId()).email(user.getEmail())
						.userName(user.getUsername()).mobileNo(user.getMobileNo()).status(user.getStatus())
						.userRole(user.getUserRole()).build();

				// Retrieve user details
				UserDetails userDetails = loadUserByUsername(userdetails2.get().getUsername());
				response.put("Authorities", userDetails.getAuthorities());
				response.put("Details", userresponse);
				return ResponseEntity.ok(response);
			} else {
				response.put("Status", HttpStatus.BAD_REQUEST.toString());
				response.put("message", messagePropertySource.messageResponse("user.notfound"));
				response.put("Error", HttpStatus.BAD_REQUEST);
				return ResponseEntity.badRequest().body(response);
			}
		} else {
			response.put("Status", HttpStatus.BAD_REQUEST.toString());
			response.put("message", "User bad request Please enter the Valid Data");
			response.put("Error", HttpStatus.BAD_REQUEST);
			return ResponseEntity.badRequest().body(response);
		}
	}

	public ResponseEntity<?> getuserdetials(String username) {
		Optional<User> details = userRepository.findByUserName(username);
		if (details.isPresent()) {
			User user = details.get();
			UserResponse response = UserResponse.builder().email(user.getEmail()).userName(user.getUsername())
					.mobileNo(user.getMobileNo()).status(user.getStatus()).build();
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not found");
		}
	}

	public Optional<User> findById(UUID userId) {
		return userRepository.findById(userId);
	}

	public Optional<User> findByUserRoleType(String admin) {
		return userRepository.findByUserRoleType(admin);
	}

	public void deleteUser(User userObject) {
		userRepository.delete(userObject);
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
