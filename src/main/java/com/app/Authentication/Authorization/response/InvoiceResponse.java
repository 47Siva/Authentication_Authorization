package com.app.Authentication.Authorization.response;

import com.app.Authentication.Authorization.dto.CustomerDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvoiceResponse {

	private CustomerDto customer;
	private Double totalprouctsAmount;
	private String gst;
	private Double gstAmount;
	private String shopDiscount;
	private Double discountAmount;
	private Double grandTotalAmount;
}
