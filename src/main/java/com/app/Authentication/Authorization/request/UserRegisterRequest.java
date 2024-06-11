package com.app.Authentication.Authorization.request;

import java.util.UUID;

import com.app.Authentication.Authorization.Deserializer.RoleEnumDeserializer;
import com.app.Authentication.Authorization.enumeration.Role;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterRequest {

	private UUID userId;
	
	private String userName;
	
	private String  email;
	
	private String mobileNo;
	
	private String password;
	
	@JsonDeserialize(using = RoleEnumDeserializer.class)
	private Role role;
	
}
