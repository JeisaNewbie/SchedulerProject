package com.example.scheduler.repository;

import com.example.scheduler.entity.ToDo;
import com.example.scheduler.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SchedulerRepository {
    User saveUser(User user);

    Optional<ToDo> saveToDo(ToDo toDo);

    Optional<User> findUserByUserId(Long userId);

    Optional<User> findUserByUserIdAndPassword(Long userId, String password);

    Optional<User> findUserByUserNameAndUserId(String userName, Long userId);

    List<ToDo> findToDoListByUserId(Long userId);

    List<ToDo> findToDoListByModifiedDate(LocalDate date);

    List<ToDo> findToDoListByTheDay(LocalDate date);

    int deleteToDo(Long toDoId);

    void deleteToDoListByUserId(Long userId);

    void deleteUser(Long id);

    int updateUser(User user);

    int updateToDo(ToDo toDo);

    Optional<ToDo> findToDoById(Long id);
}
