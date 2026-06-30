package sco3.acc.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import sco3.acc.model.User;
import sco3.acc.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> findByServiceAccounts(Set<String> accounts) {
		return userRepository.findByServiceAccount(accounts);
	}

	public Optional<User> findById(long id) {
		return userRepository.findByUserId(id);
	}
}