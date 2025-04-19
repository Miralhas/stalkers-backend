package miralhas.github.stalkers.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtDecoder jwtDecoder;
	private final JwtAuthenticationConverter jwtAuthenticationConverter;

	@Bean
	public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
		var authProvider = new DaoAuthenticationProvider(passwordEncoder());
		authProvider.setUserDetailsService(userDetailsService);
		return new ProviderManager(authProvider);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.csrf(AbstractHttpConfigurer::disable)
				.cors(AbstractHttpConfigurer::disable)
				.sessionManagement(session ->
						session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(ex -> ex
						.accessDeniedHandler(new CustomAccessDeniedHandlerImpl())
						.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
				)
				.oauth2ResourceServer(resourceServer -> {
					resourceServer.jwt(jwt -> {
						jwt.decoder(jwtDecoder);
						jwt.jwtAuthenticationConverter(jwtAuthenticationConverter);
					});
					resourceServer.accessDeniedHandler(new CustomAccessDeniedHandlerImpl());
					resourceServer.authenticationEntryPoint(new CustomAuthenticationEntryPoint());
				})
				.oauth2Login(oauth2 -> oauth2

						.successHandler((request, response, authentication) -> {
							OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
							OAuth2User oauthUser = oauthToken.getPrincipal();
							String email = oauthUser.getAttribute("email");
							String name = oauthUser.getAttribute("name");
							Map<String, Object> responseBody = new HashMap<>();
							responseBody.put("user", Map.of(
									"email", email,
									"name", name
							));
							response.setStatus(HttpServletResponse.SC_OK);
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");

							ObjectMapper mapper = new ObjectMapper();
							mapper.writeValue(response.getWriter(), responseBody);
						})
						.failureHandler(((request, response, exception) -> {
							response.setStatus(401);
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							ObjectMapper mapper = new ObjectMapper();
							mapper.writeValue(response.getWriter(), "error google oauth2");
						}))
				)
				.authorizeHttpRequests(authz -> {
					authz.requestMatchers("/secured").authenticated();
					authz.requestMatchers(HttpMethod.POST, "/api/auth/signup", "api/auth/signin").permitAll();
					authz.requestMatchers(HttpMethod.GET, "/**").permitAll();
					authz.anyRequest().authenticated();
				})
				.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
