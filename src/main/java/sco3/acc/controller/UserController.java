package sco3.acc.controller;

import static sco3.acc.common.Constants.API_V1;

import java.util.Arrays;
import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sco3.acc.dto.UserDto;
import sco3.acc.service.UserService;

@RestController
@RequestMapping(API_V1)

public class UserController {

	private final UserService service;

	public UserController(UserService service) {
		this.service = service;
	}

	@GetMapping("/users")
	public List<UserDto> findUsers( //
			@RequestParam(required = false) List<Long> userIds, //
			@RequestParam(required = false) List<String> serviceAccount //
	) throws ResponseStatusException {

		if (true //
				&& (userIds == null || userIds.isEmpty()) //
				&& (serviceAccount == null || serviceAccount.isEmpty()) //
		) {

			throw new ResponseStatusException( //
					HttpStatus.BAD_REQUEST,
					"Either 'userId' or 'serviceAccount' must be provided." //
			);
		}

		if (serviceAccount != null && serviceAccount.size() > 0) {
			return service.findByServiceAccounts(serviceAccount) //
					.stream() //
					.map(user -> new UserDto( //
							user.userId(), //
							user.serviceAccount(), //
							user.createdAt() //
					)).toList();

		}

		return Arrays.asList(new UserDto[] {});
	}
}