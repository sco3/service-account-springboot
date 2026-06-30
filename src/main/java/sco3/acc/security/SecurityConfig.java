package sco3.acc.security;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static sco3.acc.common.AccConstants.NON_SECURE_MATCHERS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain( //
			HttpSecurity http //
	) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable) //
				.sessionManagement(sm -> sm.sessionCreationPolicy(STATELESS)) //
				.authorizeHttpRequests(auth -> auth //
						.requestMatchers(NON_SECURE_MATCHERS).permitAll() //
						.anyRequest().authenticated() //
				) //
				.oauth2ResourceServer(oauth -> oauth.jwt(withDefaults())) //
				.formLogin(AbstractHttpConfigurer::disable) //
				.httpBasic(AbstractHttpConfigurer::disable);

		return http.build();
	}
}