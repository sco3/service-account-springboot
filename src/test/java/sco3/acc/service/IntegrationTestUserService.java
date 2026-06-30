package sco3.acc.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static sco3.acc.common.AccConstants.INTEGRATION;

import java.util.Map;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import sco3.acc.common.AccConstants;

@Tag(INTEGRATION)
public class IntegrationTestUserService extends IntegrationTestBase {

	@Test
	void testGetUser() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		String endpoint = SERVER_URL + AccConstants.API_V1 + "users";

		HttpClientErrorException exception = assertThrows(
				HttpClientErrorException.class, //
				() -> {
					restTemplate.exchange(endpoint, HttpMethod.GET, entity,
							Map.class);
				} //
		);

		assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		
	}
}
