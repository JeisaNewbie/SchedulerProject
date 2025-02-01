package com.example.scheduler.repository;

import com.example.scheduler.entity.ToDo;
import com.example.scheduler.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
                .name(user.getName() + "-" + key.longValue())
                .email(user.getEmail())
                .build();
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

    @Override
    public Optional<User> findUserByUserId(Long userId) {
        return jdbcTemplate.query("select * from user where id = ?", userRowMapper(), userId)
                .stream()
                .findAny();
    }

    @Override
    public Optional<User> findUserByUserIdAndPassword(Long userId, String password) {
        return jdbcTemplate.query("select * from user where id = ? and password = ?", userRowMapper(), userId, password)
                .stream()
                .findAny();
    }

    @Override
    public User findUserByUserIdAndPasswordOrElseThrow(Long userId, String password) {
        // 사용자 검증 로직 수정
        return jdbcTemplate.query("select * from user where id = ? and password = ?", userRowMapper(), userId, password)
                .stream()
                .findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자의 ID 혹은 비밀번호가 일치하지 않습니다."));
    }


    @Override
    public Optional<User> findUserByUserNameAndUserId(String userName, Long userId) {
        return jdbcTemplate.query("select * from user where name = ? and id = ?", userRowMapper(), userName, userId)
                .stream()
                .findAny();
    }

    @Override
    public List<ToDo> findToDoListByUserId(Long userId) {
        return jdbcTemplate.query("select * from to_do where user_id = ?", toDoRowMapper(), userId);
    }

    @Override
    public List<ToDo> findToDoListByModifiedDate(LocalDate date) {
        LocalDateTime theDay = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime theDayAfter = LocalDateTime.of(date.plusDays(1), LocalTime.MIN);

        return jdbcTemplate.query("select * from to_do where modified_date >= ? and modified_date < ?"
                , toDoRowMapper()
                , Timestamp.valueOf(theDay)
                , Timestamp.valueOf(theDayAfter));

    }

    @Override
    public List<ToDo> findToDoListByTheDay(LocalDate date) {
        return jdbcTemplate.query("select * from to_do where date = ?", toDoRowMapper(), Date.valueOf(date));
    }

    @Override
    public void updateUser(User user) {

        int updatedRow = jdbcTemplate.update("update user set name = ?, email = ? where id = ?", user.getName(), user.getEmail(), user.getId());

        if (updatedRow == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정할 사용자가 존재하지 않습니다 = " + user.getName() + '-' + user.getId());
        }
    }

    @Override
    public void updateToDo(ToDo toDo) {

        int updatedRow = jdbcTemplate.update("update to_do set work = ?, date = ?, modified_date = ? where id = ?",
                toDo.getWork(),
                Date.valueOf(toDo.getDate()),
                Timestamp.valueOf(toDo.getModifiedDate()),
                toDo.getId());

        if (updatedRow == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정할 일정이 존재하지 않습니다 = " + toDo.getId());
        }
    }

    @Override
    public ToDo findToDoByIdOrElseThrow(Long id) {
        return jdbcTemplate.query("select * from to_do where id = ?", toDoRowMapper(), id)
                .stream()
                .findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일정이 존재하지 않습니다 = " + id));
    }

    @Override
    public void deleteToDo(Long toDoId) {

        int deletedRow = jdbcTemplate.update("delete from to_do where id = ?", toDoId);

        if (deletedRow == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제할 일정이 존재하지 않습니다 = " + toDoId);
        }
    }

    @Override
    public void deleteToDoListByUserId(Long userId) {
        jdbcTemplate.update("delete from to_do where user_id = ?", userId);
    }

    @Override
    public void deleteUser(Long id) {
        jdbcTemplate.update("delete from user where id = ?", id);
    }

    private RowMapper<User> userRowMapper () {
        return new RowMapper<User> () {

            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                return User.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name") + '-' + rs.getLong("id"))
                        .email(rs.getString("email"))
                        .build();
            }
        };
    }

    private RowMapper<ToDo> toDoRowMapper () {
        return new RowMapper<ToDo>() {

            @Override
            public ToDo mapRow(ResultSet rs, int rowNum) throws SQLException {
                return ToDo.builder()
                        .id(rs.getLong("id"))
                        .userId(rs.getLong("user_id"))
                        .registeredDate(rs.getTimestamp("registered_date").toLocalDateTime())
                        .modifiedDate(rs.getTimestamp("modified_date").toLocalDateTime())
                        .date(rs.getDate("date").toLocalDate())
                        .work(rs.getString("work"))
                        .build();
            }
        };
    }
}
