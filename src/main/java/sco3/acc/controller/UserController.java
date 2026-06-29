package sco3.acc.controller;

import static sco3.acc.common.Constants.API_V1;

import java.util.Arrays;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sco3.acc.dto.UserDto;

@RestController
@RequestMapping(API_V1)

public class UserController {

	@GetMapping("/users")
	public List<UserDto> findUsers( //
			@RequestParam(required = false) List<Long> userIds, //
			@RequestParam(required = false) List<String> serviceAccounts //
	) throws BadRequestException {

		if (true //
				&& (userIds == null || userIds.isEmpty()) //
				&& (serviceAccounts == null || serviceAccounts.isEmpty()) //
		) {

			throw new BadRequestException( //
					"Either 'userId' or 'serviceAccount' must be provided." //
			);
		}

		return Arrays.asList(new UserDto[] {});
	}
}