package com.app.Authentication.Authorization.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminRegister {

	private String userName;

	private String email;

	private String mobileNo;

	private String password; 
}
