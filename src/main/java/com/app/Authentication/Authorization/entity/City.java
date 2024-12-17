package com.app.Authentication.Authorization.entity;

import java.io.Serializable;
import java.util.UUID;

import com.app.Authentication.Authorization.auditing.AuditwithBaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name = "city_details")
@Schema(description = "Details about the City")
public class City extends AuditwithBaseEntity implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UUID id;

	@Column(name = "name")
	private String cityName;

	@Column(name = "short_desc")
	private String shortName;

	@Column(name = "zip_code")
	private String zipCode;
	
    @ManyToOne(targetEntity = State.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "state_id", nullable = false) // Foreign Key column
    private State state;
	
	
}
