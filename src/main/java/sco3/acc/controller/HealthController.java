package sco3.acc.controller;

import static sco3.acc.common.Constants.API_V1;

import java.util.List;
import java.util.Map;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping(API_V1 + "health")
public class HealthController {
	@GetMapping("/me")
	public Map<String, Object> me(JwtAuthenticationToken auth) {

		Jwt jwt = auth.getToken();

		Map<String, Object> realmAccess = jwt.getClaim("realm_access");

		@SuppressWarnings("unchecked")
		List<String> roles = (List<String>) realmAccess.get("roles");

		return Map.of( //
				"username", jwt.getClaimAsString("preferred_username"), //
				"subject", jwt.getSubject(), //
				"issuer", jwt.getIssuer().toString(), //
				"roles", roles//
		);
	}
}
