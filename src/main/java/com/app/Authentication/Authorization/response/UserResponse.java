package com.app.Authentication.Authorization.response;

import com.app.Authentication.Authorization.enumeration.UserStatus;
import com.app.Authentication.Authorization.request.UserRegisterRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

	private String userName;
	
	private String email;

	private String mobileNo;
	
	private UserStatus	status;
}
