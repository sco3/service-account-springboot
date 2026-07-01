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
	private static final String PARAM_USER_IDS = "userIds";
	private static final String PARAM_USER_ID = "userId";
	private static final String PARAM_ACCOUNTS = "accounts";

	private static final String SELECT_BASE = """
			SELECT user_id, service_account, created_at
			FROM service_accounts.users
			""";

	private static final String FIND_BY_USER_ID = SELECT_BASE + "WHERE user_id = :"
			+ PARAM_USER_ID;
	private static final String FIND_BY_ACCOUNTS = SELECT_BASE
			+ "WHERE service_account IN (:" + PARAM_ACCOUNTS + ")";
	private static final String FIND_BY_USER_IDS = SELECT_BASE + "WHERE user_id IN (:"
			+ PARAM_USER_IDS + ")";
	private final NamedParameterJdbcTemplate jdbc;

	public JdbcUserRepository(NamedParameterJdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	User mapRow(ResultSet rs, int rowNum) throws java.sql.SQLException {
		return new User( //
				rs.getLong("user_id"), //
				rs.getString("service_account"), //
				rs.getTimestamp("created_at").toLocalDateTime()//
		);
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

		return jdbc.query(sql, params, this::mapRow);
	}

	@Override
	public List<User> findAll() {
		return jdbc.query("""
				SELECT user_id, service_account, created_at
				FROM service_accounts.users
				""", this::mapRow);
	}

	@Override
	public List<User> findByUserIds(Set<Long> userIds) {

		MapSqlParameterSource params = new MapSqlParameterSource()//
				.addValue(PARAM_USER_IDS, userIds);

		return jdbc.query(FIND_BY_USER_IDS, params, this::mapRow);
	}

	@Override
	public Optional<User> findByUserId(Long userId) {
		var sql = """
				SELECT user_id, service_account, created_at
				FROM service_accounts.users
				WHERE user_id = :userId
				""";

		MapSqlParameterSource params = new MapSqlParameterSource()//
				.addValue("userId", userId);

		return jdbc.query( //
				sql, params, this::mapRow //
		).stream().findFirst();
	}
}