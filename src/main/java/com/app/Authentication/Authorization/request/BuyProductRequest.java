package com.app.Authentication.Authorization.request;

import java.util.List;

import com.app.Authentication.Authorization.enumeration.GenderType;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class BuyProductRequest {

	private String customerName;

	private String email;

	private String mobileNo;

	private String address;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyy")
	private String date;

	private GenderType gender;

	private List<CustomerProductRequest> customerProduct;
}
