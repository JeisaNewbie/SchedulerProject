package com.example.scheduler.controller;

import com.example.scheduler.comparator.ModifiedDateComparator;
import com.example.scheduler.comparator.NameComparator;
import com.example.scheduler.comparator.TheDayComparator;
import com.example.scheduler.dto.*;
import com.example.scheduler.entity.ToDo;
import com.example.scheduler.entity.User;
import com.example.scheduler.service.SchedulerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/schedule")
public class SchedulerController {
    private final SchedulerService schedulerService;

    public SchedulerController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    // 유저 email 과 password 로 해당유저가 등록한 모든 일정 조회
    @PostMapping("/users")
    public ResponseEntity<List<ToDoResponseDto>> findScheduleByEmailAndPassword(@RequestBody UserRequestDto dto) {

        User user = User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();

        return new ResponseEntity<>(schedulerService.findScheduleByUserInfo(user), HttpStatus.OK);
    }

    // 사용자 명 으로 해당 날짜의 모든 일정 조회
    @GetMapping("/users/{name}-{id}")
    public ResponseEntity<List<ToDoResponseDto>> findScheduleByName(
            @PathVariable String name,
            @PathVariable Long id,
            @RequestParam(defaultValue = "asc") String order
            ) {
        List<ToDoResponseDto> list = schedulerService.findScheduleByUserNameAndUserId(name, id);

        if ("desc".equals(order)) {
            list.sort(new NameComparator().reversed());
        }

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // 수정일 로 해당 날짜의 모든 일정 조회
    @GetMapping("/modified-date/{date}")
    public ResponseEntity<List<ToDoResponseDto>> findScheduleByModifiedDate(
            @PathVariable("date") LocalDate date,
            @RequestParam(defaultValue = "asc") String order
    ) {
        List<ToDoResponseDto> list = schedulerService.findScheduleByModifiedDate(date);

        if ("desc".equals(order)) {
            list.sort(new ModifiedDateComparator().reversed());
        }

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // D-DAY 로 해당 날짜의 모든 일정 조회
    @GetMapping("/the-day/{date}")
    public ResponseEntity<List<ToDoResponseDto>> findScheduleByTheDay(
            @PathVariable("date") LocalDate date,
            @RequestParam(defaultValue = "asc") String order
    ) {

         List<ToDoResponseDto> list = schedulerService.findScheduleByTheDay(date);

        if ("desc".equals(order)) {
            list.sort(new TheDayComparator().reversed());
        }

        return new ResponseEntity<>(list, HttpStatus.OK);
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
