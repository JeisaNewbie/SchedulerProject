package com.example.scheduler.repository;

import com.example.scheduler.entity.Paging;
import com.example.scheduler.entity.ToDo;
import com.example.scheduler.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SchedulerRepository {
    User saveUser(User user);

    ToDo saveToDo(ToDo toDo);

    Optional<User> findUserByUserId(Long userId);

    Optional<User> findUserByUserIdAndPassword(Long userId, Integer password);

    User findUserByUserIdAndPasswordOrElseThrow(Long userId, Integer password);

    Optional<User> findUserByUserNameAndUserId(String userName, Long userId);

    ToDo findToDoByIdOrElseThrow(Long id);

    List<ToDo> findToDoListByUserId(Long userId);

    List<ToDo> findToDoListByModifiedDate(LocalDate date);

    List<ToDo> findToDoListByTheDay(LocalDate date, Paging paging);

    void deleteToDo(Long toDoId);

    void deleteToDoListByUserId(Long userId);

    void deleteUser(Long id);

    void updateUser(User user);

    void updateToDo(ToDo toDo);

}
