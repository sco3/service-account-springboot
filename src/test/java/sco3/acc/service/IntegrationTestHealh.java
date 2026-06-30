package sco3.acc.service;

import static java.lang.System.out;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static sco3.acc.common.AccConstants.API_HEALTH_ME_ENDPOINT;
import static sco3.acc.common.AccConstants.INTEGRATION;

import java.util.Map;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Tag(INTEGRATION)

public class IntegrationTestHealh extends IntegrationTestBase {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testGetHealth() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		String endpoint = SERVER_URL + API_HEALTH_ME_ENDPOINT;
		ResponseEntity<Map> response = restTemplate.exchange(endpoint, HttpMethod.GET,
				entity, Map.class);

		assertThat(response.getStatusCode().equals(HttpStatus.OK));
		var body = response.getBody();
		out.println(body);
		assertThat (body).isNotNull();
		
		assertThat(body.get("roles")).asInstanceOf(LIST).contains("admin");

	}
}
