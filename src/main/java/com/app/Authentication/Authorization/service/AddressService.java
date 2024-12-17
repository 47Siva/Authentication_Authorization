package com.app.Authentication.Authorization.service;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.Authentication.Authorization.entity.City;
import com.app.Authentication.Authorization.entity.Country;
import com.app.Authentication.Authorization.entity.State;
import com.app.Authentication.Authorization.repository.CityRepository;
import com.app.Authentication.Authorization.repository.CountryRepository;
import com.app.Authentication.Authorization.repository.StateRepository;
import com.app.Authentication.Authorization.response.MessageService;
import com.app.Authentication.Authorization.response.Response;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {

	private final CountryRepository countryRepository;
	private final StateRepository stateRepository;
	private final CityRepository cityRepository;
	private @NonNull MessageService messagePropertySource;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public void saveOrUpdate(Country object) {
		if (countryRepository.findByCountryName(object.getCountryName()).isPresent()) {
			throw new IllegalArgumentException("Country already exists");
		}
		Country country = Country.builder().countryCode(object.getCountryCode()).countryName(object.getCountryName())
				.shortName(object.getShortName()).build();
		countryRepository.save(country);
	}

	public ResponseEntity<?> addState(State object) throws Exception {
		Country country = countryRepository.findByCountryName(object.getCountry().getCountryName())
				.orElseThrow(() -> new IllegalArgumentException("Country not found"));

		String stateName = object.getStateName();
		String countryName = object.getCountry().getCountryName();
		UUID countryId = object.getCountry().getId();

		Optional<State> state = stateRepository.findByStateName(stateName);
		Optional<State> stateCountry = stateRepository.findByCountry(countryId);

//	        // Check if the state already exists for the given country
	        if (stateRepository.findByStateNameAndCountryCode(stateName, countryId).isPresent()) {
	            throw new IllegalArgumentException("State already exists in the given country");
	        }

//	        Optional<State> existingState = stateRepository.findByNameAndCountry(stateName, countryId);

//		if (state.isPresent() && stateCountry.isPresent()) {
//			throw new IllegalArgumentException("State already exists in the given country");
//		}

		return ResponseEntity.ok( stateRepository.save(object));
	}

	public City addCity(City city) {
//         City citytb =cityRepository.findByCityAndStateName(city.getState().getStateName(),city.getState().getId())
//        		.orElseThrow(()-> new IllegalArgumentException("State and city alreday exists"));

		UUID stateName = city.getState().getId();
		String cityName = city.getCityName();
		
//		Optional<City> state = cityRepository.findByState(stateName);
//		Optional<City> citytb = cityRepository.findByCity(cityName);
		
		if (cityRepository.findByCityNameAndStateId(cityName, stateName).isPresent()) {
            throw new IllegalArgumentException("City already exists in the given state");
        }
		
//		if (state.isPresent() && citytb.isPresent()) {
//			throw new IllegalArgumentException("State already exists in the given country");
//		}
		
		return cityRepository.save(city);
	}

}
