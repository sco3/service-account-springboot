package sco3.acc.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import sco3.acc.common.AccConstants;

public class IntegrationTestUserService {

	private final RestTemplate restTemplate = new RestTemplate();

	private final String SERVER_URL = "http://localhost:8080";
	private final String KEYCLOAK_URL = "http://localhost:8081";

	private String accessToken;

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

	@Test
	void testGetUser() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		String endpoint = SERVER_URL + AccConstants.API_V1 + "users";

		HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, //
				() -> {
					restTemplate.exchange(endpoint, HttpMethod.GET, entity, Map.class);
				} //
		);

		assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
}
