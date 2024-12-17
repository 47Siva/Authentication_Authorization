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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name = "state_details")
@Schema(description = "Details about the State")
public class State extends AuditwithBaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private UUID id;

	@Column(name = "name")
	private String stateName;

	@Column(name = "short_name")
	private String shortName;
	
    @ManyToOne(targetEntity = Country.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "country_id", nullable = false) // Foreign Key column
    private Country country;
	

//    @OneToMany(targetEntity = City.class,cascade = {CascadeType.ALL})
//    @JoinColumn(name = "state_id",referencedColumnName = "id", nullable = false)
//	private List<City> city;
}
