package com.example.scheduler.repository;

import com.example.scheduler.entity.ToDo;
import com.example.scheduler.entity.User;

import java.util.Optional;

public interface SchedulerRepository {
    User saveUser(User user);

    Optional<User> findUserByEmailAndPassword(User user);

    Optional<ToDo> saveToDo(ToDo toDo);

}
