package com.app.Authentication.Authorization.response;

import java.util.List;

import lombok.Data;

@Data
public class Error {
	
	private String code;
	private String reason;
	private List<String> errorList;

	@Override
	public String toString() {
		return "Error [code=" + code + ", Reason=" + reason + "]";
	}
}
