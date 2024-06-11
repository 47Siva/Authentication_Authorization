package com.app.Authentication.Authorization.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.security.auth.Subject;

import org.springframework.stereotype.Service;

import com.app.Authentication.Authorization.advice.ObjectInvalidException;
import com.app.Authentication.Authorization.entity.User;
import com.app.Authentication.Authorization.enumeration.RequestType;
import com.app.Authentication.Authorization.request.UserRegisterRequest;
import com.app.Authentication.Authorization.response.MessageService;
import com.app.Authentication.Authorization.service.UserService;
import com.app.Authentication.Authorization.util.ValidationUtil;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserValidator {

	MessageService messageSource;

	private @NonNull UserService userService;

	List<String> errors = null;
	List<String> errorsObj = null;
	Optional<Subject> subject = null;

	public ValidationResult validate(RequestType requestType, UserRegisterRequest request) {

		errors = new ArrayList<>();
		ValidationResult result = new ValidationResult();
		User user = null;
		
		if (requestType.equals(RequestType.PUT)) {
			if (!ValidationUtil.isNull(request.getUserId())) {
				throw new ObjectInvalidException(messageSource.messageResponse("invalid.request.payload"));
			}
			if (ValidationUtil.isNullOrEmpty(request.getUserName())) {
				errors.add(messageSource.messageResponse("register.user.name.required"));
			}else {
				request.setUserName((ValidationUtil.getFormattedString(request.getUserName())));   
				if (!ValidationUtil.isUserNameValid(request.getUserName())) {
					errors.add(messageSource.messageResponse("register.user.name.invalid"));
				}
			}
			Optional<User> userOptional = userService.getByMobileNo(request.getMobileNo());
			if (userOptional.isPresent()) {
				String[] params = new String[] { request.getMobileNo() };
				errors.add(messageSource.messageResponse("register.mobileno.exist", params));
			}
			
			Optional<User> userDuplicateObj = userService.findByUserName(request.getUserName());
			if (userDuplicateObj.isPresent()) {
				String[] params = new String[] { request.getUserName() };
				errors.add(messageSource.messageResponse("user.name.duplicate", params));
			}
			Optional<User> userDuplicateMailObj = userService.findByUserEmail(request.getEmail());
			if (userDuplicateMailObj.isPresent()) {				
				errors.add(messageSource.messageResponse("user.register.email.duplicate"));
			}
		}else {
			if (ValidationUtil.isNull(request.getUserId()))
				throw new ObjectInvalidException(messageSource.messageResponse("invalid.request.payload"));
			Optional<User> userOptional = userService.findById(request.getUserId());
			if (!userOptional.isPresent()) {
				throw new ObjectInvalidException(messageSource.messageResponse("user.not.found"));
			}
			user = userOptional.get();
			
			if (ValidationUtil.isNullOrEmpty(request.getUserName())) {
				errors.add(messageSource.messageResponse("register.user.name.required"));
			} else {
				request.setUserName(ValidationUtil.getFormattedString(request.getUserName()));
				if (!ValidationUtil.isUserNameValid(request.getUserName())) {
					errors.add(messageSource.messageResponse("full.name.invalid"));
				}
			}
			if (ValidationUtil.isNullOrEmpty(request.getEmail())) {
				errors.add(messageSource.messageResponse("register.email.required"));
			} else {
				request.setEmail(ValidationUtil.getFormattedString(request.getEmail()));
				if (!ValidationUtil.isValidEmailId(request.getEmail())) {
					errors.add(messageSource.messageResponse("register.email.invalid "));
				}
			}
			if (ValidationUtil.isNullObject(request.getRole())) {
				errors.add(messageSource.messageResponse("user.type.required"));
			}
			if (ValidationUtil.isValidPhoneNo(request.getMobileNo())) {
				if (ValidationUtil.isNullOrEmpty(request.getMobileNo())) {
					errors.add(messageSource.messageResponse("register.mobile.required"));
				}
				errors.add(messageSource.messageResponse("user.phone.number.invalid"));
				}else {
					request.setMobileNo(ValidationUtil.getFormattedString(request.getMobileNo()));
					if (!ValidationUtil.isValidMobileNumber(request.getMobileNo())) {
						errors.add(messageSource.messageResponse("user.phone.invalid"));
					}
				}	
		}
	}
}
