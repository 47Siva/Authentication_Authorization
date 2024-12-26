package com.app.Authentication.Authorization.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.Authentication.Authorization.entity.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, UUID>{

	@Query(value = "select * from country_details where name =:name ",nativeQuery = true)
	Optional<Country> findByCountryName(String name);

}
