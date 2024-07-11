package com.app.Authentication.Authorization.entity;

import java.io.Serializable;
import java.util.UUID;

import com.app.Authentication.Authorization.auditing.AuditwithBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name = "product_details")
public class Product extends AuditwithBaseEntity implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "product_id")
	private UUID id;
	
	@Column(name = "product_name")
	private String productName;
	
	@Column(name = "available_quantity")
	private int availableQuantity;
	
	@Column(name = "product_price")
	private long price;
	
}
