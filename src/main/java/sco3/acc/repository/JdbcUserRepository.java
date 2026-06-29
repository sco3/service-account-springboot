package sco3.acc.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sco3.acc.model.User;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbc;

    public JdbcUserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static User mapRow(ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new User(
                rs.getLong("user_id"),
                rs.getString("service_account"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }

    @Override
    public Optional<User> findByUserId(long userId) {
        var list = jdbc.query(
                """
                SELECT user_id, service_account, created_at
                FROM service_accounts.users
                WHERE user_id = ?
                """,
                JdbcUserRepository::mapRow,
                userId
        );

        return list.stream().findFirst();
    }

    @Override
    public List<User> findByServiceAccount(String serviceAccount) {
        return jdbc.query(
                """
                SELECT user_id, service_account, created_at
                FROM service_accounts.users
                WHERE service_account = ?
                """,
                JdbcUserRepository::mapRow,
                serviceAccount
        );
    }

    @Override
    public List<User> findAll() {
        return jdbc.query(
                """
                SELECT user_id, service_account, created_at
                FROM service_accounts.users
                """,
                JdbcUserRepository::mapRow
        );
    }
}