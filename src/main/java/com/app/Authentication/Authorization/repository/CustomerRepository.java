package com.app.Authentication.Authorization.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.Authentication.Authorization.entity.Customer;
import com.app.Authentication.Authorization.entity.User;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID>{

	@Query(value = "SELECT * FROM customer_details where email =:email ", nativeQuery = true)
	Optional<Customer> findByDuplicateEamil(String email);

	@Query(value = "SELECT * FROM customer_details where mobile_no =:mobileNo ", nativeQuery = true)
	Optional<Customer> findByDuplicateNumber(String mobileNo);

}
