package com.example.scheduler.repository;

import com.example.scheduler.entity.ToDo;
import com.example.scheduler.entity.User;

import java.util.List;
import java.util.Optional;

public interface SchedulerRepository {
    User saveUser(User user);

    Optional<User> findUserByEmailAndPassword(String email, String password);

    Optional<ToDo> saveToDo(ToDo toDo);

    Optional<List<ToDo>> findToDoListByUser(User savedUser);
}
