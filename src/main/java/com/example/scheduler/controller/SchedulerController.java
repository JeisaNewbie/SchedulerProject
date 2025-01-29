package com.example.scheduler.controller;

import com.example.scheduler.dto.ScheduleRequestDto;
import com.example.scheduler.dto.ScheduleResponseDto;
import com.example.scheduler.dto.ToDoResponseDto;
import com.example.scheduler.dto.UserResponseDto;
import com.example.scheduler.entity.ToDo;
import com.example.scheduler.entity.User;
import com.example.scheduler.service.SchedulerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/schedule")
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
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto dto) {
        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();

        ToDo toDo = ToDo.builder()
                .registeredDate(now)
                .modifiedDate(now)
                .date(dto.getDate())
                .work(dto.getWork())
                .build();

        return new ResponseEntity<>(schedulerService.saveSchedule(user, toDo), HttpStatus.CREATED);
    }

    @PatchMapping("/users/{id}/to-dos/{toDoId}") // 일정 수정
    public ResponseEntity<ToDoResponseDto> updateToDo(
            @PathVariable Long id,
            @PathVariable Long toDoId,
            @RequestBody ScheduleRequestDto scheduleRequestDto
    )
    {
        return new ResponseEntity<>(schedulerService.updateToDo(), HttpStatus.OK);
    }

    @PatchMapping("/users/{id}/user") // 유저 정보 수정
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable("id") Long id, @RequestBody ScheduleRequestDto scheduleRequestDto) {
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
