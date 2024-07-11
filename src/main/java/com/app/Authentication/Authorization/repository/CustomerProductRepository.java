package com.app.Authentication.Authorization.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.Authentication.Authorization.entity.CustomerProduct;

@Repository
public interface CustomerProductRepository extends JpaRepository<CustomerProduct, UUID>{

}
