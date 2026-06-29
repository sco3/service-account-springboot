package sco3.acc.security;

import static sco3.acc.common.AccConstants.NON_SECURE_MATCHERS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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
}