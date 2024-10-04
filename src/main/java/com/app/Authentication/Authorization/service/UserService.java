package com.app.Authentication.Authorization.service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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
		String enpassword = PasswordUtil.getEncryptedPassword(user.getPassword());

		User userobj = User.builder().email(user.getEmail()).mobileNo(user.getMobileNo())
				.password(enpassword).userRole(user.getUserRole())
				.status(Status.ACTIVE).applicationUserName(user.getUsername()).build();
		User savedUser = userRepository.saveAndFlush(userobj);
		if (user.getUserRole().equals(Role.CUSTOMER)) {
			var customer = Customer.builder().email(userobj.getEmail()).mobileNo(userobj.getMobileNo())
					.customerName(userobj.getApplicationUserName()).status(userobj.getStatus())
					.userId(savedUser.getId()).build();
			customerRepository.save(customer);
		}
		
		return savedUser;
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
				
				// Check if the email matches
		        if (!useremail.equals(user.getEmail())) {
		            return ResponseEntity.badRequest().body(Map.of(
		                "Status", HttpStatus.BAD_REQUEST.toString(),
		                "message", "User bad request. Please enter valid data.",
		                "Error", HttpStatus.BAD_REQUEST
		            ));
		        }
		        
		     // Define acceptable roles
		        List<Role> acceptableRoles = Arrays.asList(Role.USER, Role.ADMIN,Role.CUSTOMER);
		        
		     // Check if the user's role is in the acceptable roles
		        if (!acceptableRoles.contains(user.getUserRole())) {
		            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
		                "Status", HttpStatus.FORBIDDEN.toString(),
		                "message", "User does not have permission to access this request. Allowed roles: " + acceptableRoles,
		                "Error", HttpStatus.FORBIDDEN
		            ));
		        }

//				if (!user.getUserRole().equals(Role.USER)) {
//					response.put("Status", HttpStatus.NOT_ACCEPTABLE.toString());
//					response.put("message", "User can't access the request");
//					response.put("Error", HttpStatus.NOT_ACCEPTABLE);
//					return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
//				}
		        
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

	
}
