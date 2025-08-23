package miralhas.github.stalkers.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.servlet.ServletContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI myOpenAPI(ServletContext servletContext) {
		Server server = new Server().url(servletContext.getContextPath());

		Contact contact = new Contact();
		contact.setEmail("stalkers@gmail.com");
		contact.setName("Stalkers Corp.");

		License mitLicense = new License().name("MIT License")
				.url("https://choosealicense.com/licenses/mit/");

		Info info = new Info()
				.title("Stalkers API")
				.version("1.0")
				.contact(contact)
				.description("This API exposes endpoints to the Stalkers API.")
				.license(mitLicense);

		SecurityScheme securityScheme = new SecurityScheme()
				.type(SecurityScheme.Type.HTTP)
				.scheme("bearer")
				.bearerFormat("JWT");

		SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearer-key");

		return new OpenAPI()
				.info(info)
				.servers(List.of(server))
				.components(new Components().addSecuritySchemes("bearer-key", securityScheme))
				.addSecurityItem(securityRequirement);
	}
}
