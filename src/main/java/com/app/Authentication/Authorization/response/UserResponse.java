package com.app.Authentication.Authorization.response;

import java.util.UUID;

import com.app.Authentication.Authorization.enumeration.Role;
import com.app.Authentication.Authorization.enumeration.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
	
	private UUID userId;
	
	private String userName;
	
	private String email;

	private String mobileNo;
	
	private Status	status;
	
	private Role userRole;
}
