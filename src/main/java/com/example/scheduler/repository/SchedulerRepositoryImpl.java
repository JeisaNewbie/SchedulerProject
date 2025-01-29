package com.example.scheduler.repository;

import com.example.scheduler.entity.ToDo;
import com.example.scheduler.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class SchedulerRepositoryImpl implements SchedulerRepository {
    JdbcTemplate jdbcTemplate;

    public SchedulerRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User saveUser(User user) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("user")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("name", user.getName());
        params.put("email", user.getEmail());
        params.put("password", user.getPassword());

        Number key = insert.executeAndReturnKey(params);

        return User.builder()
                .id(key.longValue())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    @Override
    public Optional<User> findUserByEmailAndPassword(User user) {
        List<User> ret = jdbcTemplate.query("select * from user where email = ? and password = ?", userRowMapper(), user.getEmail(), user.getPassword());
        return ret.stream().findAny();
    }

    @Override
    public Optional<ToDo> saveToDo(ToDo toDo) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("to_do")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", toDo.getUser().getId());
        params.put("date", Date.valueOf(toDo.getDate()));
        params.put("registered_date", Timestamp.valueOf(toDo.getRegisteredDate()));
        params.put("modified_date", Timestamp.valueOf(toDo.getModifiedDate()));
        params.put("work", toDo.getWork());

        Number key = insert.executeAndReturnKey(params);

        return Optional.ofNullable(ToDo.builder()
                .id(key.longValue())
                .date(toDo.getDate())
                .registeredDate(toDo.getRegisteredDate())
                .modifiedDate(toDo.getModifiedDate())
                .work(toDo.getWork())
                .build());
    }

    private RowMapper<User> userRowMapper () {
        return new RowMapper<User> () {

            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                return User.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .email(rs.getString("email"))
                        .build();
            }
        };
    }
}
