package sco3.acc.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import sco3.acc.model.User;

public interface UserRepository {
	Optional<User> findByUserId(Long userIds);

	List<User> findByServiceAccount(Set<String> serviceAccount);

	List<User> findByUserIds(Set<Long> userIds);

	List<User> findAll();
}