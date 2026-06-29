package sco3.acc.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import sco3.acc.controller.UserController;
import sco3.acc.model.User;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

	private static final String SERVICE_ACCOUNT = "serviceAccount";

	private static final String FIRST = "$[0]." + SERVICE_ACCOUNT;

	private static final String MONITORING_AGENT = "monitoring-agent";

	@Autowired
	MockMvc mockMvc;

	@MockitoBean
	UserService service;

	@Test
	void shouldReturnUsers() throws Exception {
		var user = new User(1L, MONITORING_AGENT, LocalDateTime.now());

		Mockito.when(service.findByServiceAccounts(List.of(MONITORING_AGENT))) //
				.thenReturn(List.of(user));

		mockMvc.perform(get("/api/v1/users") //
				.param(SERVICE_ACCOUNT, MONITORING_AGENT)) //
				.andExpect(status().isOk())//
				.andExpect(jsonPath(FIRST).value(MONITORING_AGENT));
	}

	@Test
	void shouldNotReturnUsers() throws Exception {

		Mockito.when(service.findByServiceAccounts(null)) //
				.thenThrow(BadRequestException.class);

		mockMvc.perform(get("/api/v1/users")) //
				.andExpect(status().isBadRequest());
	}

}