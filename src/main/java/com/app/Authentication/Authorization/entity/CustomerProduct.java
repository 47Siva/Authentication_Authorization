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
@Table(name = "customer_buyed_product")
public class CustomerProduct extends AuditwithBaseEntity implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UUID id;
	
	@Column(name = "date")
	private String date;

	@Column(name = "product_name")
	private String productName;
	
	@Column(name = "product_quantity")
	private int quantity;
	
	@Column(name = "product_price")
	private double price;
	
	@Column(name = "total_amount")
	private double totalAmount;
}
