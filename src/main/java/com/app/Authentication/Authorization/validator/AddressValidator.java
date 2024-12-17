package com.app.Authentication.Authorization.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.security.auth.Subject;

import org.springframework.stereotype.Service;

import com.app.Authentication.Authorization.advice.ObjectInvalidException;
import com.app.Authentication.Authorization.entity.City;
import com.app.Authentication.Authorization.entity.Country;
import com.app.Authentication.Authorization.entity.State;
import com.app.Authentication.Authorization.repository.CityRepository;
import com.app.Authentication.Authorization.repository.CountryRepository;
import com.app.Authentication.Authorization.repository.StateRepository;
import com.app.Authentication.Authorization.request.AddressRequest;
import com.app.Authentication.Authorization.service.AddressService;
import com.app.Authentication.Authorization.util.ValidationUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AddressValidator {

    private final AddressService addressService;
    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;
//    private final StateService stateService;
//    private final CityService cityService;

	List<String> errors = null;
	List<String> errorsObj = null;
	Optional<Subject> subject = null;

    
    public ValidationResult validateCountry(AddressRequest.CountryRequest request) {
        errors = new ArrayList<>();
		ValidationResult result = new ValidationResult();
		Country country= null;
        
        if (ValidationUtil.isNullOrEmpty(request.getName())) {
            errors.add("Country name is required.");
        } else if (!ValidationUtil.isNameValid(request.getName())) {
            errors.add("Invalid country name. It must contain only letters and spaces.");
        }

        if (!errors.isEmpty()) {
            throw new ObjectInvalidException(String.join(", ", errors));
        }
        
        country = Country.builder().countryName(request.getName())
        		.shortName(request.getName()).countryCode(request.getCountryCode()).build();
        result.setObject(country);
        return result;
    }

    public ValidationResult validateState(AddressRequest.StateRequest request) {
    	 errors = new ArrayList<>();
 		ValidationResult result = new ValidationResult();
 		State state= null;

        if (ValidationUtil.isNullOrEmpty(request.getName())) {
            errors.add("State name is required.");
        } else if (!ValidationUtil.isNameValid(request.getName())) {
            errors.add("Invalid state name. It must contain only letters and spaces.");
        }

        if (ValidationUtil.isNullOrEmpty(request.getCountryName())) {
            errors.add("Country name is required for adding a state.");
        } 
        
        Country country = countryRepository.findByCountryName(request.getCountryName())
        		.orElseThrow(() -> new IllegalArgumentException("Country not found"));
        
        
        if (!errors.isEmpty()) {
            throw new ObjectInvalidException(String.join(", ", errors));
        }
        
        state = State.builder().stateName(request.getName()).
				shortName(request.getName()).country(country) .build();
        result.setObject(state);
        return result;
    }
//
    public ValidationResult validateCity(AddressRequest.CityRequest request) {
    	 errors = new ArrayList<>();
 		ValidationResult result = new ValidationResult();
 		City city= null;

        if (ValidationUtil.isNullOrEmpty(request.getName())) {
            errors.add("City name is required.");
        } else if (!ValidationUtil.isNameValid(request.getName())) {
            errors.add("Invalid city name. It must contain only letters and spaces.");
        }

        if (ValidationUtil.isNullOrEmpty(request.getStateName())) {
            errors.add("State name is required for adding a city.");
        } 
        State state = stateRepository.findByStateName(request.getStateName())
            		.orElseThrow(() -> new IllegalArgumentException("State not found"));
        

        if (ValidationUtil.isNullOrEmpty(request.getCountryName())) {
            errors.add("Country name is required for adding a city.");
        } 
        Country countrytb = countryRepository.findByCountryName(request.getCountryName())
            		.orElseThrow(() -> new IllegalArgumentException("Country not found"));

        if (!errors.isEmpty()) {
            throw new ObjectInvalidException(String.join(", ", errors));
        }
        
        city = City.builder().cityName(request.getName()).shortName(request.getName())
        		.state(state).build();
        result.setObject(city);
        return result;
    }
}
