package com.app.Authentication.Authorization.response;

import java.util.UUID;

import com.app.Authentication.Authorization.enumeration.GenderType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {

	private UUID id;
	
	private UUID userId;
	
	private String customerName;
	
	private String email;
	
	private String mobileNo;
	
	private String address;
	
	private GenderType gender;
}
