package com.example.scheduler.service;


import com.example.scheduler.dto.SchedulerRequestDto;
import com.example.scheduler.dto.SchedulerResponseDto;
import com.example.scheduler.dto.ToDoResponseDto;
import com.example.scheduler.dto.UserResponseDto;

import java.util.List;

public interface SchedulerService {
    List<ToDoResponseDto> findScheduleById(Long id);
    List<ToDoResponseDto> findScheduleByModifiedDate(String date);
    List<ToDoResponseDto> findScheduleByDay(String date);
    SchedulerResponseDto saveSchedule(SchedulerRequestDto schedulerRequestDto);
    ToDoResponseDto updateToDo();
    UserResponseDto updateUser();
    void deleteUser();

    void deleteToDoById(Long id);
}
