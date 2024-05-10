package com.app.Authentication.Authorization.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info =@Info(
		contact = @Contact(
				name = "Ebrain",
				email = "",
				url = ""
				),
		description = "Authentication & Authorization",
		title = "Authentication-Authorization",
		version = "1.0",
		license = @License(name = "Licence name",url = "https//some-url.com"),
		termsOfService = "terms and service"),
		servers = {
		@Server(description ="http",url ="http://localhost:8080")
		}
)
@SecurityScheme(
		name = "bearAuth",
		description = "jwt Auth Descrption",
		scheme = "Bearer",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		in = SecuritySchemeIn.HEADER
)
public class SwaggerConfiguration {

}
