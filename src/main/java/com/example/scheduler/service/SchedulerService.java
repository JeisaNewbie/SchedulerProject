package com.example.scheduler.service;


import com.example.scheduler.dto.ScheduleResponseDto;
import com.example.scheduler.dto.ToDoResponseDto;
import com.example.scheduler.dto.UserResponseDto;
import com.example.scheduler.entity.ToDo;
import com.example.scheduler.entity.User;

import java.util.List;

public interface SchedulerService {
    List<ToDoResponseDto> findScheduleById(Long id);
    List<ToDoResponseDto> findScheduleByModifiedDate(String date);
    List<ToDoResponseDto> findScheduleByDay(String date);
    ScheduleResponseDto saveSchedule(User user, ToDo toDo);
    ToDoResponseDto updateToDo();
    UserResponseDto updateUser();
    void deleteUser();

    void deleteToDoById(Long id);
}
