package sco3.acc.security;

import static sco3.acc.common.AccConstants.NON_SECURE_MATCHERS;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri:}")
	private String issuerUri;

	@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri:}")
	private String jwkSetUri;

	@SuppressWarnings("unused")
	@Bean
	public SecurityFilterChain securityFilterChain(//
			HttpSecurity http //
	) throws Exception {

		http.csrf(csrf -> csrf.disable()) //
				.sessionManagement(sm -> sm.sessionCreationPolicy( //
						SessionCreationPolicy.STATELESS //
				)) //
				.authorizeHttpRequests(auth -> auth.requestMatchers(NON_SECURE_MATCHERS)//
						.permitAll() //
						.anyRequest() //
						.authenticated() //
				) //
				.oauth2ResourceServer(oauth -> oauth.jwt(jwt -> {
				})) //
				.formLogin(form -> form.disable()) //
				.httpBasic(basic -> basic.disable()); //

		return http.build();
	}

	@Bean
	public JwtDecoder jwtDecoder() {
		if (jwkSetUri != null && !jwkSetUri.isBlank()) {
			NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
			if (issuerUri != null && !issuerUri.isBlank()) {
				OAuth2TokenValidator<Jwt> issuerValidator = new JwtIssuerValidator(issuerUri);
				decoder.setJwtValidator(
						new DelegatingOAuth2TokenValidator<>(issuerValidator));
			}
			return decoder;
		}
		return JwtDecoders.fromIssuerLocation(issuerUri);
	}
}