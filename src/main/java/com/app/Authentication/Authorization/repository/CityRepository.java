package com.app.Authentication.Authorization.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.Authentication.Authorization.entity.City;

@Repository
public interface CityRepository extends JpaRepository<City, UUID> {
	
	@Query(value = "select * from city_details where name =:cityName && state_id =:id ",nativeQuery = true)
	Optional<City> findByCityAndStateName(String cityName, UUID id);
	
	@Query("SELECT c FROM City c WHERE c.cityName = :cityName AND c.state.id = :stateId")
	Optional<City> findByCityNameAndStateId(@Param("cityName") String cityName, @Param("stateId") UUID stateId);


	@Query(value = "select * from city_details where state_id =:stateName",nativeQuery = true)
	Optional<City> findByState(UUID stateName);

	@Query(value = "select * from city_details where name =:cityName",nativeQuery = true)
	Optional<City> findByCity(String cityName);

}
