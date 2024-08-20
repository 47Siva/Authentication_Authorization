package com.app.Authentication.Authorization.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.security.auth.Subject;

import org.springframework.stereotype.Service;

import com.app.Authentication.Authorization.advice.ObjectInvalidException;
import com.app.Authentication.Authorization.entity.Customer;
import com.app.Authentication.Authorization.enumeration.RequestType;
import com.app.Authentication.Authorization.repository.CustomerRepository;
import com.app.Authentication.Authorization.request.BuyProductRequest;
import com.app.Authentication.Authorization.response.MessageService;
import com.app.Authentication.Authorization.security.JwtService;
import com.app.Authentication.Authorization.service.CustomerServicee;
import com.app.Authentication.Authorization.util.ValidationUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerValidator {

	MessageService messageService;
	private final JwtService jwtService;
	private final CustomerServicee customerService;
	private final CustomerRepository customerRepository;

	List<String> errors = null;
	List<String> errorsObj = null;
	Optional<Subject> subject = null;

	public ValidationResult validate(RequestType requestType, BuyProductRequest request,String auth,UUID id) {

		errors = new ArrayList<>();
		ValidationResult result = new ValidationResult();
		Customer customer = null;
		

		if (requestType.equals(RequestType.PUT)) {
			Optional<Customer> customerOptional = customerRepository.findById(id);
			customer = customerOptional.get();
//			String token = jwtService.extractToken(auth);
//			String tokenUserName = jwtService.extractUserName(token);
//
//			if (!tokenUserName.equals(request.getEmail())) {
//				throw new ObjectInvalidException(messageService.messageResponse("user.token.name"));
//			}

			if (id == null) {
				throw new ObjectInvalidException(messageService.messageResponse("invalid.payload.request"));
			}

			
			if (ValidationUtil.isNullOrEmpty(request.getCustomerName())) {
				errors.add(messageService.messageResponse("register.user.name.required"));
			} else {
				if (!ValidationUtil.isUserNameValid(request.getCustomerName())) {
					errors.add(messageService.messageResponse("register.user.name.invalid"));
				}
			}

			if (ValidationUtil.isNullOrEmpty(ValidationUtil.getFormattedString(request.getEmail()))) {
				errors.add(messageService.messageResponse("register.email.required"));
			} else {
				request.setEmail(ValidationUtil.getFormattedString(request.getEmail()));
				if (!ValidationUtil.isValidEmailId(request.getEmail())) {
					errors.add(messageService.messageResponse("register.email.invalid"));
				}
			}

			Optional<Customer> userDuplicateEmail = customerService.findByDuplicateEmail(request.getEmail());
			if (userDuplicateEmail.isPresent()) {
				if (userDuplicateEmail.get().getEmail().equals(request.getEmail())) {
					request.setEmail(request.getEmail());
				} else {
					errors.add(messageService.messageResponse("user.register.email.duplicate"));
				}
			}

			if (ValidationUtil.isNullOrEmpty(request.getMobileNo())) {
				errors.add(messageService.messageResponse("register.mobile.required"));
			} else {
				request.setMobileNo(ValidationUtil.getFormattedString(request.getMobileNo()));
				if (!ValidationUtil.isValidMobileNumber(request.getMobileNo())) {
					errors.add(messageService.messageResponse("register.mobile.invalid"));
				}
			}

			if (ValidationUtil.isNullOrEmpty(request.getAddress())) {
				errors.add(messageService.messageResponse("register.address.required"));
			} else {
				request.setAddress((ValidationUtil.getFormattedString(request.getAddress())));
				if (!ValidationUtil.isValidAddress(request.getAddress())) {
					errors.add(messageService.messageResponse("register.address.invalid"));
				}
			}

			Optional<Customer> userExist = customerService.getMobileNos(request.getMobileNo());
			if (userExist.isPresent()) {
				if (userExist.get().getMobileNo().equals(request.getMobileNo())) {
					request.setMobileNo(request.getMobileNo());
				} else {
					String[] params = new String[] { request.getMobileNo() };
					errors.add(messageService.messageResponse("register.mobileno.exist", params));
				}
			}

			if (ValidationUtil.isGenderrequired(request.getGender())) {
				errors.add(messageService.messageResponse("register.gender.required"));
			} else {
				request.setGender(ValidationUtil.getFormattedGender(request.getGender()));
				if (!ValidationUtil.isGenderValid(request.getGender())) {
					errors.add(messageService.messageResponse("register.gender.invalid"));
				}
			}

			if (request.getDate() != null) {
				request.setDate(request.getDate());
			} else {
				LocalDate date = LocalDate.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				request.setDate(date.format(formatter));
			}
			if (errors.size() > 0) {
				String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
				throw new ObjectInvalidException(errorMessage);
			}
			
			customer.setCustomerName(request.getCustomerName());
			customer.setEmail(request.getEmail());
			customer.setMobileNo(request.getMobileNo());
			customer.setAddress(request.getAddress());
			customer.setGender(request.getGender());
			
			result.setObject(customer);
		}

		if (requestType.equals(RequestType.POST)) {
			if (id!=null)
				throw new ObjectInvalidException(messageService.messageResponse("invalid.payload.request"));

			if (customer != null) {
				throw new ObjectInvalidException(messageService.messageResponse("user.exist"));
			}
			if (ValidationUtil.isNullOrEmpty(request.getCustomerName())) {
				errors.add(messageService.messageResponse("register.user.name.required"));
			} else {
				if (!ValidationUtil.isUserNameValid(request.getCustomerName())) {
					errors.add(messageService.messageResponse("register.user.name.invalid"));
				}
			}

			if (ValidationUtil.isNullOrEmpty(ValidationUtil.getFormattedString(request.getEmail()))) {
				errors.add(messageService.messageResponse("register.email.required"));
			} else {
				request.setEmail(ValidationUtil.getFormattedString(request.getEmail()));
				if (!ValidationUtil.isValidEmailId(request.getEmail())) {
					errors.add(messageService.messageResponse("register.email.invalid"));
				}
			}

			Optional<Customer> userDuplicateMailObj = customerService.findByDuplicateEmail(request.getEmail());
			if (userDuplicateMailObj.isPresent()) {
				errors.add(messageService.messageResponse("user.register.email.duplicate"));
			}

			if (ValidationUtil.isNullOrEmpty(request.getMobileNo())) {
				errors.add(messageService.messageResponse("register.mobile.required"));
			} else {
				request.setMobileNo(ValidationUtil.getFormattedString(request.getMobileNo()));
				if (!ValidationUtil.isValidMobileNumber(request.getMobileNo())) {
					errors.add(messageService.messageResponse("register.mobile.invalid"));
				}
			}

			if (ValidationUtil.isNullOrEmpty(request.getAddress())) {
				errors.add(messageService.messageResponse("register.address.required"));
			} else {
				request.setAddress((ValidationUtil.getFormattedString(request.getAddress())));
				if (!ValidationUtil.isValidAddress(request.getAddress())) {
					errors.add(messageService.messageResponse("register.address.invalid"));
				}
			}

			Optional<Customer> userExist = customerService.getMobileNos(request.getMobileNo());
			if (userExist.isPresent()) {
				String[] params = new String[] { request.getMobileNo() };
				errors.add(messageService.messageResponse("register.mobileno.exist", params));
			}

			if (ValidationUtil.isGenderrequired(request.getGender())) {
				errors.add(messageService.messageResponse("register.gender.required"));
			} else {
				request.setGender(ValidationUtil.getFormattedGender(request.getGender()));
				if (!ValidationUtil.isGenderValid(request.getGender())) {
					errors.add(messageService.messageResponse("register.gender.invalid"));
				}
			}

			if (request.getDate() != null) {
				request.setDate(request.getDate());
			} else {
				LocalDate date = LocalDate.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				request.setDate(date.format(formatter));
			}

			if (errors.size() > 0) {
				String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
				throw new ObjectInvalidException(errorMessage);
			}
			
			customer = Customer.builder().email(request.getEmail()).address(request.getAddress())
					.customerName(request.getCustomerName()).date(request.getDate()).mobileNo(request.getMobileNo())
					.gender(request.getGender()).build();
			result.setObject(customer);
		}

//		 List<CustomerProduct> customerProducts = CustomerProductConverter.convert(request.getCustomerProduct());
		return result;
	}
}
