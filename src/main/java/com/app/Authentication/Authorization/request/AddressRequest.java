package com.app.Authentication.Authorization.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AddressRequest {
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class CountryRequest {
		private String name; // Country name
		private String countryCode;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class StateRequest {
		private String name; // State name
		private String countryName; // Related country name
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class CityRequest {
		private String name; // City name
		private String stateName; // Related state name
		private String countryName; // Related country name
		private String zipCode;
	}
}
