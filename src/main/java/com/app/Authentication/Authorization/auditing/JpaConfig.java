package com.app.Authentication.Authorization.auditing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.scheduling.annotation.EnableScheduling;


@Configuration
@EnableScheduling
public class JpaConfig {

	@Bean
	public AuditorAware<String> auditorProvider(){
		return new  ApplicationAuditAware();
	}
}
