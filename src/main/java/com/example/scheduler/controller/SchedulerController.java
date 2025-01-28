package com.example.scheduler.controller;

import com.example.scheduler.dto.SchedulerRequestDto;
import com.example.scheduler.dto.SchedulerResponseDto;
import com.example.scheduler.dto.ToDoResponseDto;
import com.example.scheduler.dto.UserResponseDto;
import com.example.scheduler.service.SchedulerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController("/api/schedule")
public class SchedulerController {
    private final SchedulerService schedulerService;

    public SchedulerController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @GetMapping("/users/{id}") // 유저id로 해당유저가 등록한 모든 일정 조회
    public ResponseEntity<List<ToDoResponseDto>> findScheduleById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(new ArrayList<>(schedulerService.findScheduleById(id)), HttpStatus.OK);
    }

    @GetMapping("/modified-date/{date}") // 수정일 로 해당 날짜의 모든 일정 조회
    public ResponseEntity<List<ToDoResponseDto>> findScheduleByModifiedDate(@PathVariable("date") String date) {
        return new ResponseEntity<>(new ArrayList<>(schedulerService.findScheduleByModifiedDate(date)), HttpStatus.OK);
    }

    @GetMapping("/d-day/{date}") // D-DAY 로 해당 날짜의 모든 일정 조회
    public ResponseEntity<List<ToDoResponseDto>> findScheduleByDay(@PathVariable("date") String date) {
        return new ResponseEntity<>(new ArrayList<>(schedulerService.findScheduleByDay(date)), HttpStatus.OK);
    }

    @PostMapping // 일정 생성 to_do(registerd_date modified_date work) user (name password email) schedule (date)
    public ResponseEntity<SchedulerResponseDto> createSchedule(@RequestBody SchedulerRequestDto schedulerRequestDto) {
        return new ResponseEntity<>(schedulerService.saveSchedule(schedulerRequestDto), HttpStatus.OK);
    }

    @PatchMapping("/users/{id}/to-dos/{toDoId}") // 일정 수정
    public ResponseEntity<ToDoResponseDto> updateToDo(
            @PathVariable Long id,
            @PathVariable Long toDoId,
            @RequestBody SchedulerRequestDto schedulerRequestDto
    )
    {
        return new ResponseEntity<>(schedulerService.updateToDo(), HttpStatus.OK);
    }

    @PatchMapping("/users/{id}/user") // 유저 정보 수정
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable("id") Long id, @RequestBody SchedulerRequestDto schedulerRequestDto) {
        return new ResponseEntity<>(schedulerService.updateUser(), HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}") // 유저와 해당 유저의 모든 일정 삭제
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        schedulerService.deleteUser();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}/to-dos/{toDoId}") // 해당 유저의 특정 일정 삭제
    public ResponseEntity<Void> deleteToDo(@PathVariable Long id, @PathVariable Long toDoId) {
        schedulerService.deleteToDoById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
