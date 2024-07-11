package com.app.Authentication.Authorization.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.app.Authentication.Authorization.auditing.AuditwithBaseEntity;
import com.app.Authentication.Authorization.enumeration.GenderType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
@Table(name = "customer_details")
public class Customer extends AuditwithBaseEntity implements Serializable{


	@Serial
	private static final long serialVersionUID = 1L;

	private UUID id;

	@Column(name = "user_id")
	private UUID userId;
	
	@Column(name = "customer_name")
	private String customerName;
	
	@Column(name = "email")
	private String email;

	@Column(name = "mobile_no")
	private String mobileNo;

	@Column(name = "address")
	private String address;
	
	@Column(name = "date")
	private String date;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "gender")
	private GenderType gender;
	
	@OneToMany(targetEntity = CustomerProduct.class,cascade = {CascadeType.ALL})
	@JoinColumn(name = "customer_id",referencedColumnName = "id")
	private List<CustomerProduct> customerProducet;
}
