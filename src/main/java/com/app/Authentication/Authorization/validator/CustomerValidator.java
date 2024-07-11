package com.app.Authentication.Authorization.validator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.security.auth.Subject;

import org.springframework.stereotype.Service;

import com.app.Authentication.Authorization.Deserializer.CustomerProductConverter;
import com.app.Authentication.Authorization.advice.ObjectInvalidException;
import com.app.Authentication.Authorization.entity.Customer;
import com.app.Authentication.Authorization.entity.CustomerProduct;
import com.app.Authentication.Authorization.entity.User;
import com.app.Authentication.Authorization.enumeration.RequestType;
import com.app.Authentication.Authorization.repository.CustomerRepository;
import com.app.Authentication.Authorization.request.BuyProductRequest;
import com.app.Authentication.Authorization.request.UserRegisterRequest;
import com.app.Authentication.Authorization.response.MessageService;
import com.app.Authentication.Authorization.security.JwtService;
import com.app.Authentication.Authorization.service.CustomerServicee;
import com.app.Authentication.Authorization.service.InvoiceService;
import com.app.Authentication.Authorization.service.UserService;
import com.app.Authentication.Authorization.util.ValidationUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerValidator {
	
	MessageService messageService;
	private final CustomerServicee customerService;
	
	List<String> errors = null;
	List<String> errorsObj = null;
	Optional<Subject> subject = null;
	
	public ValidationResult validate(RequestType requestType, BuyProductRequest request) {
		
		errors = new ArrayList<>();
		ValidationResult result = new ValidationResult();
		Customer customer = null;
		
		if (ValidationUtil.isNullOrEmpty(request.getCustomerName())) {
			errors.add(messageService.messageResponse("register.user.name.required"));
		} else {
			request.setCustomerName(ValidationUtil.getFormattedString(request.getCustomerName()));
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
		
		if(ValidationUtil.isGenderrequired(request.getGender())) {
			errors.add(messageService.messageResponse("register.gender.required"));
		}else {
			request.setGender(ValidationUtil.getFormattedGender(request.getGender()));
			if(!ValidationUtil.isGenderValid(request.getGender())) {
				errors.add(messageService.messageResponse("register.gender.invalid"));
			}
		}
		
		if (errors.size() > 0) {
			String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
			throw new ObjectInvalidException(errorMessage);
		}
		
//		 List<CustomerProduct> customerProducts = CustomerProductConverter.convert(request.getCustomerProduct());

		customer = Customer.builder().email(request.getEmail()).address(request.getAddress())
				.customerName(request.getCustomerName()).date(LocalDate.now().toString())
				.mobileNo(request.getMobileNo()).gender(request.getGender()).build();
		result.setObject(customer);
		return result;
	}
}
