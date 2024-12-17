package com.app.Authentication.Authorization.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.Authentication.Authorization.entity.State;

@Repository
public interface StateRepository extends JpaRepository<State, UUID> ,CrudRepository<State, UUID>{

//	@Query(value = "select * from state_details where name =:stateName AND country_id =:countryid ",nativeQuery = true)
//	Optional<State> findByNameAndCountry(String stateName, UUID countryid);
	
//	@Query(value = "SELECT * FROM state_details s WHERE s.name = :stateName AND s.country_id = :countryId", nativeQuery = true)
//	Optional<State> findByStateNameAndCountryId(@Param("stateName") String stateName, @Param("countryId") UUID countryId);
	
	@Query("SELECT s FROM State s WHERE s.stateName = :stateName AND s.country.id = :countryCode")
	Optional<State> findByStateNameAndCountryCode(@Param("stateName") String stateName, @Param("countryCode") UUID countryCode);



	@Query(value = "select * from state_details where name =:stateName",nativeQuery = true)
	Optional<State> findByStateName(String stateName);

	@Query(value = "select * from state_details where country_id =:countryId",nativeQuery = true)
	Optional<State> findByCountry(UUID countryId);

}
