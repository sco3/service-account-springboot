package sco3.acc.repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import sco3.acc.model.User;

@Repository
public class JdbcUserRepository implements UserRepository {

	private final NamedParameterJdbcTemplate jdbc;

	public JdbcUserRepository(NamedParameterJdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	private static User mapRow(ResultSet rs, int rowNum)
			throws java.sql.SQLException {
		return new User( //
				rs.getLong("user_id"), //
				rs.getString("service_account"), //
				rs.getTimestamp("created_at").toLocalDateTime()//
		);
	}

	@Override
	public Optional<User> findByUserId(long userId) {
		var sql = """
				SELECT user_id, service_account, created_at
				FROM service_accounts.users
				WHERE user_id = :userId
				""";

		MapSqlParameterSource params = new MapSqlParameterSource()//
				.addValue("userId", userId);

		return jdbc.query( //
				sql, params, JdbcUserRepository::mapRow //
		).stream().findFirst();
	}

	@Override
	public List<User> findByServiceAccount(Set<String> serviceAccount) {

		String sql = """
				SELECT user_id, service_account, created_at
				FROM service_accounts.users
				WHERE service_account IN (:accounts)
				""";

		MapSqlParameterSource params = new MapSqlParameterSource()//
				.addValue("accounts", serviceAccount);

		return jdbc.query(sql, params, JdbcUserRepository::mapRow);
	}

	@Override
	public List<User> findAll() {
		return jdbc.query("""
				SELECT user_id, service_account, created_at
				FROM service_accounts.users
				""", JdbcUserRepository::mapRow);
	}
}