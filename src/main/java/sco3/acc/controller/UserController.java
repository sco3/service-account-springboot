package sco3.acc.controller;

import static sco3.acc.common.AccConstants.API_V1;
import static sco3.acc.common.CollectionChecker.hasItems;
import static sco3.acc.common.CollectionChecker.isEmpty;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import sco3.acc.dto.UserDto;
import sco3.acc.model.User;
import sco3.acc.service.UserService;

@RestController
@RequestMapping(API_V1)

public class UserController {

	private final UserService service;

	public UserController(UserService service) {
		this.service = service;
	}

	UserDto toUserDto(User user) {
		return new UserDto( //
				user.userId(), //
				user.serviceAccount(), //
				user.createdAt() //
		);
	}

	@GetMapping("/users")
	public List<UserDto> findUsers( //
			@RequestParam(required = false) List<Long> userIds, //
			@RequestParam(required = false) List<String> serviceAccount //
	) throws ResponseStatusException {
		if (isEmpty(userIds, serviceAccount)) {
			throw new ResponseStatusException( //
					HttpStatus.BAD_REQUEST,
					"Either 'userId' or 'serviceAccount' must be provided." //
			);
		}

		if (hasItems(serviceAccount)) {
			return service.findByServiceAccounts(serviceAccount) //
					.stream().map(this::toUserDto).toList();

		}

		return Arrays.asList(new UserDto[] {});
	}
}