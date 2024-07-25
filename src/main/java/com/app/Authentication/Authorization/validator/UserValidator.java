package com.app.Authentication.Authorization.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.security.auth.Subject;

import org.springframework.stereotype.Service;

import com.app.Authentication.Authorization.advice.ObjectInvalidException;
import com.app.Authentication.Authorization.entity.User;
import com.app.Authentication.Authorization.enumeration.RequestType;
import com.app.Authentication.Authorization.request.UserRegisterRequest;
import com.app.Authentication.Authorization.response.MessageService;
import com.app.Authentication.Authorization.security.JwtService;
import com.app.Authentication.Authorization.service.UserService;
import com.app.Authentication.Authorization.util.PasswordUtil;
import com.app.Authentication.Authorization.util.ValidationUtil;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserValidator {

	MessageService messageSource;
	private final JwtService jwtService;

	private @NonNull UserService userService;
	

	List<String> errors = null;
	List<String> errorsObj = null;
	Optional<Subject> subject = null;

	public ValidationResult validate(RequestType requestType, UserRegisterRequest request , String auth) {

		errors = new ArrayList<>();
		ValidationResult result = new ValidationResult();
		User user = null;
		
		Optional<User> userOptional = userService.findById(request.getUserId());
		user = userOptional.get();

		String token = jwtService.extractToken(auth);
		String tokenusername = jwtService.extractUserName(token);
		
		if(!tokenusername.equals(request.getUserName())) {
			throw new ObjectInvalidException(messageSource.messageResponse("user.token.name"));
		}
		
		if (requestType.equals(RequestType.PUT)) {
			if (ValidationUtil.isNull(request.getUserId())) {
				throw new ObjectInvalidException(messageSource.messageResponse("invalid.payload.request"));
			}
			if (ValidationUtil.isNullOrEmpty(request.getUserName())) {
				errors.add(messageSource.messageResponse("register.user.name.required"));
			} else {
				if (!ValidationUtil.isUserNameValid(request.getUserName())) {
					errors.add(messageSource.messageResponse("register.user.name.invalid"));
				}
			}
			if (ValidationUtil.isNullOrEmpty(request.getMobileNo())) {
				errors.add(messageSource.messageResponse("register.mobile.required"));
			} else {
				request.setMobileNo(ValidationUtil.getFormattedString(request.getMobileNo()));
				if (!ValidationUtil.isValidMobileNumber(request.getMobileNo())) {
					errors.add(messageSource.messageResponse("register.mobile.invalid"));
				}
			}
			
			if (ValidationUtil.isNullOrEmpty(ValidationUtil.getFormattedString(request.getEmail()))) {
				errors.add(messageSource.messageResponse("register.email.required"));
			} else {
				request.setEmail(ValidationUtil.getFormattedString(request.getEmail()));
				if (!ValidationUtil.isValidEmailId(request.getEmail())) {
					errors.add(messageSource.messageResponse("register.email.invalid"));
				}
			}
			
			if (ValidationUtil.isNullOrEmpty(request.getPassword())) {
				errors.add(messageSource.messageResponse("register.password.required"));
			} else if(ValidationUtil.isNullOrEmpty(request.getConfirmPassword())) {
				errors.add(messageSource.messageResponse("register.confirmPassword.required"));
			}else {
				request.setPassword(ValidationUtil.getFormattedString(request.getPassword()));
				if (!ValidationUtil.isValidPassword(request.getPassword())) {
					errors.add(messageSource.messageResponse("register.password.invalid"));
				}
				if (!request.getPassword().equals(request.getConfirmPassword())) {
					errors.add(messageSource.messageResponse("password.mismatch"));
				}
			}
			
			if(ValidationUtil.isRolerequired(request.getUserRole())) {
				errors.add(messageSource.messageResponse("register.role.required"));
			}else {
				request.setUserRole(ValidationUtil.getFormattedRole(request.getUserRole()));
				
				if(!ValidationUtil.isRoleValid(request.getUserRole())) {
					errors.add(messageSource.messageResponse("register.role.invalid"));
				}
			}
		}
		
		else {
			if (!ValidationUtil.isNull(request.getUserId()))
				throw new ObjectInvalidException(messageSource.messageResponse("invalid.request.payload"));

			if (!userOptional.isPresent()) {
				throw new ObjectInvalidException(messageSource.messageResponse("user.not.found"));
			}
			
		}
		if (ValidationUtil.isNullOrEmpty(request.getUserName())) {
			errors.add(messageSource.messageResponse("register.user.name.required"));
		} else {
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
		if (ValidationUtil.isNullObject(request.getUserRole())) {
			errors.add(messageSource.messageResponse("user.type.required"));
		}
		if (ValidationUtil.isValidPhoneNo(request.getMobileNo())) {
			if (ValidationUtil.isNullOrEmpty(request.getMobileNo())) {
				errors.add(messageSource.messageResponse("register.mobile.required"));
			}
			errors.add(messageSource.messageResponse("user.phone.number.invalid"));
		} else {
			request.setMobileNo(ValidationUtil.getFormattedString(request.getMobileNo()));
			if (!ValidationUtil.isValidMobileNumber(request.getMobileNo())) {
				errors.add(messageSource.messageResponse("user.phone.invalid"));
			}
		}
		if (ValidationUtil.isNullOrEmpty(request.getPassword())) {
			errors.add(messageSource.messageResponse("register.password.required"));
		}
		if (!request.getPassword().equals(request.getConfirmPassword())) {
			errors.add(messageSource.messageResponse("password.mismatch"));
		}
		String encrptPassword = PasswordUtil.getEncryptedPassword(request.getPassword());
		if (errors.size() > 0) {
			String errorMessage = errors.stream().map(a -> String.valueOf(a)).collect(Collectors.joining(", "));
			throw new ObjectInvalidException(errorMessage);
		}
		if (null == user) {
			String admin="ADMIN";
			Optional<User> findbyrole=userService.findByUserRoleType(admin);
		
			
			user = User.builder().userName(request.getUserName())
					.userRole(request.getUserRole()).email(request.getEmail())
					.userName(request.getUserName()).password(encrptPassword).mobileNo(request.getMobileNo()).build();
		} else {
			user.setUserName(request.getUserName());
			user.setUserRole(request.getUserRole());
			user.setEmail(request.getEmail());
			user.setMobileNo(request.getMobileNo());
			user.setPassword(encrptPassword);
		}
		result.setObject(user);
		return result;
	}
}
