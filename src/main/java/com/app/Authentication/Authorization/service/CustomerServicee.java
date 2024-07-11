package com.app.Authentication.Authorization.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.Authentication.Authorization.entity.Customer;
import com.app.Authentication.Authorization.entity.User;
import com.app.Authentication.Authorization.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServicee {

	private final CustomerRepository customerRepository;

	public Optional<Customer> findByDuplicateEmail(String email) {
		return customerRepository.findByDuplicateEamil(email);
	}

	public Optional<Customer> getMobileNos(String mobileNo) {
		return customerRepository.findByDuplicateNumber(mobileNo);
	}
	
	
}
