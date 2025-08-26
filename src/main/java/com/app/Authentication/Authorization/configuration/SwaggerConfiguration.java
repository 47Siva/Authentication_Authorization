
package com.app.Authentication.Authorization.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
//import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info =@Info(
		contact = @Contact(
				name = "Ebrain",
				email = "",
				url = ""
				),
		title = "Application Service API",
		description = "Provides core services like Authentication, User, Product, Address and Invoice management",
		version = "1.0",
		license = @License(name = "Licence name",url = "https//some-url.com"),
		termsOfService = "terms and service"),
//		servers = {
//		@Server(description ="http",url ="http://localhost:8081")
//		},
		security = { @SecurityRequirement(name = "bearAuth") }
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
