package com.example.scheduler.service;

import com.example.scheduler.dto.SchedulerRequestDto;
import com.example.scheduler.dto.SchedulerResponseDto;
import com.example.scheduler.dto.ToDoResponseDto;
import com.example.scheduler.dto.UserResponseDto;
import com.example.scheduler.repository.SchedulerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchedulerServiceImpl implements SchedulerService {
    private final SchedulerRepository schedulerRepository;

    public SchedulerServiceImpl(SchedulerRepository schedulerRepository) {
        this.schedulerRepository = schedulerRepository;
    }

    @Override // 유저id로 해당유저가 등록한 모든 일정 조회
    public List<ToDoResponseDto> findScheduleById(Long id) {
        return List.of();
    }

    @Override // 수정일 로 해당 날짜의 모든 일정 조회
    public List<ToDoResponseDto> findScheduleByModifiedDate(String date) {
        return List.of();
    }

    @Override // D-DAY 로 해당 날짜의 모든 일정 조회
    public List<ToDoResponseDto> findScheduleByDay(String date) {
        return List.of();
    }

    @Override // 일정 생성 to_do(registerd_date modified_date work) user (name password email) schedule (date)
    public SchedulerResponseDto saveSchedule(SchedulerRequestDto schedulerRequestDto) {
        return null;
    }

    @Override
    public ToDoResponseDto updateToDo() {
        return null;
    }

    @Override
    public UserResponseDto updateUser() {
        return null;
    }

    @Override
    public void deleteUser() {

    }

    @Override
    public void deleteToDoById(Long id) {

    }
}
