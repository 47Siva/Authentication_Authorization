package com.app.Authentication.Authorization.auditing;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {

	@Id
	@GeneratedValue(strategy =GenerationType.UUID )
	@GenericGenerator(name = "UUID2",
					  type = org.hibernate.id.uuid.UuidGenerator.class)
	@JdbcTypeCode(SqlTypes.CHAR)
	@Column(name = "id",updatable = false, nullable = false)
	private UUID id;
}
