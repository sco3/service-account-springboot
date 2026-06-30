package sco3.acc.repository;

import sco3.acc.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository {

	Optional<User> findByUserId(long userId);

	List<User> findByServiceAccount(Set<String> serviceAccount);

	List<User> findAll();
}