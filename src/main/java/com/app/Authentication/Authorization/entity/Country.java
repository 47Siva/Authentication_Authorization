package com.app.Authentication.Authorization.entity;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.app.Authentication.Authorization.auditing.AuditwithBaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "country_details")
@Schema(description = "Details about the Countru")
public class Country extends AuditwithBaseEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private UUID id;

	@Column(name = "name")
	private String countryName;

	@Column(name = "short_name")
	private String shortName;

	@Column(name = "country_code")
	private String countryCode;

}
