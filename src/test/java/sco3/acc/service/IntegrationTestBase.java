package sco3.acc.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Tag("integration")
public class IntegrationTestBase {
	public static final String SERVER_URL = "http://localhost:8080";
	public static final String KEYCLOAK_URL = "http://localhost:8081";
	
	protected final RestTemplate restTemplate = new RestTemplate();
	protected String accessToken;

	@BeforeEach
	void authenticateWithKeycloak() {
		String tokenUrl = KEYCLOAK_URL + "/realms/service-accounts/protocol/openid-connect/token";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("client_id", "service-api");
		body.add("client_secret", "service-api-secret");
		body.add("username", "admin");
		body.add("password", "admin");
		body.add("grant_type", "password");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		this.accessToken = (String) response.getBody().get("access_token");
		
		System.out.println("Token obtained");
	}


}
