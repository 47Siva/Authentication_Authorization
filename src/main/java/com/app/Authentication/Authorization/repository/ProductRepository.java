package com.app.Authentication.Authorization.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.Authentication.Authorization.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>{

	@Query(value = "SELECT * FROM product_details WHERE product_name =:productName", nativeQuery = true)
	Optional<Product> findByName(String productName);

}
