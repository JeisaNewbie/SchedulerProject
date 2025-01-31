package com.example.scheduler.service;


import com.example.scheduler.dto.ScheduleResponseDto;
import com.example.scheduler.dto.ToDoResponseDto;
import com.example.scheduler.dto.UserResponseDto;
import com.example.scheduler.entity.ToDo;
import com.example.scheduler.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface SchedulerService {
    List<ToDoResponseDto> findScheduleByModifiedDate(LocalDate date);
    List<ToDoResponseDto> findScheduleByTheDay(LocalDate date);
    ScheduleResponseDto saveSchedule(User user, ToDo toDo);
    ToDoResponseDto updateToDo();
    UserResponseDto updateUser();
    void deleteUser(User user);

    void deleteToDo(User user, Long toDoId);

    List<ToDoResponseDto> findScheduleByUserNameAndUserId(String userName, Long userId);
}
