package com.app.Authentication.Authorization.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.Authentication.Authorization.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
	
	@Query(value = "SELECT * FROM user_details where user_name =:username",nativeQuery = true)
	Optional<User> findByUserName(String username);

	@Query(value = "SELECT * FROM user_details where email =:email ", nativeQuery = true)
	Optional<User> findByDuplicateEamil(String email);

	@Query(value = "SELECT * FROM user_details where user_name =:userName ", nativeQuery = true)
	Optional<User> findByDuplicateUserName(String userName);

	@Query(value = "SELECT * FROM user_details where mobile_no =:phoneNumber ", nativeQuery = true)
	Optional<User> findByDuplicateNumber(String phoneNumber);
	
	@Query(value = "SELECT * FROM user_details where password =:password ", nativeQuery = true)
	Optional<User> findByDuplicatePassword(String password);

	@Query(value = "select * from user_details where mobile_no =:mobileNo ", nativeQuery = true)
	Optional<User> getByMobileNo(String mobileNo);

	@Query(value = "select * from user_details where email =:email ", nativeQuery = true)
	Optional<User> findByUserEmail(String email);

	@Query(value = "SELECT * FROM user_details where role=:admin", nativeQuery = true)
	Optional<User> findByUserRoleType(String admin);
}
