package com.app.Authentication.Authorization.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.app.Authentication.Authorization.auditing.AuditwithBaseEntity;
import com.app.Authentication.Authorization.enumeration.Role;
import com.app.Authentication.Authorization.enumeration.Status;
import com.app.Authentication.Authorization.util.PasswordUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "user_details")
public class User extends AuditwithBaseEntity implements UserDetails, Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private UUID id;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "email")
	private String email;

	@Column(name = "mobile_no")
	private String mobileNo;

	@Column(name = "password")
	@NotNull(message = "Password")
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private Role userRole;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status	status;
	

	public void setAndEncryptPassword(String password) {
		setPassword(PasswordUtil.getEncryptedPassword(password));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(userRole.name()));
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
