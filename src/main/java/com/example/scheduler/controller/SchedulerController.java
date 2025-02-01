package com.example.scheduler.controller;

import com.example.scheduler.comparator.ModifiedDateComparator;
import com.example.scheduler.comparator.TheDayComparator;
import com.example.scheduler.dto.request.ScheduleRequestDto;
import com.example.scheduler.dto.request.UserRequestDto;
import com.example.scheduler.dto.response.ScheduleResponseDto;
import com.example.scheduler.dto.response.ToDoResponseDto;
import com.example.scheduler.dto.response.UserResponseDto;
import com.example.scheduler.entity.Paging;
import com.example.scheduler.entity.ToDo;
import com.example.scheduler.entity.User;
import com.example.scheduler.service.SchedulerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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

    // 사용자 명 으로 해당 날짜의 모든 일정 조회
    @GetMapping("/users/{name}-{id}")
    public ResponseEntity<List<ToDoResponseDto>> findScheduleByName(
            @PathVariable String name,
            @Min(value = 1, message = "ID 값은 1 이상이어야 합니다.") @PathVariable Long id
    ) {
        return ResponseEntity.ok(schedulerService.findScheduleByUserNameAndUserId(name, id));
    }

    // 수정일 로 해당 날짜의 모든 일정 조회
    @GetMapping("/modified-date/{date}")
    public ResponseEntity<List<ToDoResponseDto>> findScheduleByModifiedDate(
            @PathVariable LocalDate date,
            @RequestParam(defaultValue = "asc") String order
    ) {
        List<ToDoResponseDto> list = schedulerService.findScheduleByModifiedDate(date);

        if ("desc".equals(order)) {
            list.sort(new ModifiedDateComparator().reversed());
        }

        return ResponseEntity.ok(list);
    }

    // D-DAY 로 해당 날짜의 모든 일정 조회 (Pagination 기능 추가)
    @GetMapping("/the-day/{date}")
    public ResponseEntity<List<ToDoResponseDto>> findScheduleByTheDay(
            @PathVariable LocalDate date,
            @Min(value = 1, message = "page 값은 1 이상이어야 합니다.") @RequestParam(defaultValue = "1") String page,
            @Min(value = 1, message = "size 값은 10 이상이어야 합니다.") @RequestParam(defaultValue = "10") String size,
            @RequestParam(defaultValue = "asc") String order
    ) {

        Paging paging = new Paging(Long.parseLong(page), Long.parseLong(size));

        List<ToDoResponseDto> list = schedulerService.findScheduleByTheDay(date, paging);

        if ("desc".equals(order)) {
            list.sort(new TheDayComparator().reversed());
        }

        return ResponseEntity.ok(list);
    }

    // 사용자 및 일정 생성
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(
            @Valid @RequestBody ScheduleRequestDto dto
    ) {
        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
                .name(dto.getUser().getName())
                .email(dto.getUser().getEmail())
                .password(dto.getUser().getPassword())
                .build();

        ToDo toDo = ToDo.builder()
                .id(dto.getToDo().getId())
                .registeredDate(now)
                .modifiedDate(now)
                .date(dto.getToDo().getDate())
                .work(dto.getToDo().getWork())
                .build();

        return ResponseEntity.ok(schedulerService.saveSchedule(user, toDo));
    }

    @PostMapping("/users/to-dos")
    public ResponseEntity<ScheduleResponseDto> createToDo(
            @Valid @RequestBody ScheduleRequestDto dto
    ) {
        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
                .id(dto.getUser().getId())
                .name(dto.getUser().getName())
                .email(dto.getUser().getEmail())
                .password(dto.getUser().getPassword())
                .build();

        ToDo toDo = ToDo.builder()
                .id(dto.getToDo().getId())
                .registeredDate(now)
                .modifiedDate(now)
                .date(dto.getToDo().getDate())
                .work(dto.getToDo().getWork())
                .build();

        return ResponseEntity.ok(schedulerService.saveToDo(user, toDo));
    }

    // 사용자 정보 수정
    @PatchMapping("/users")
    public ResponseEntity<UserResponseDto> updateUser(
            @Valid @RequestBody UserRequestDto dto
    ) {

        User user = User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();

        return ResponseEntity.ok(schedulerService.updateUser(user));
    }

    // 일정 수정
    @PatchMapping("/users/to-dos")
    public ResponseEntity<ToDoResponseDto> updateToDo(
            @Valid @RequestBody ScheduleRequestDto dto
    )
    {
        User user = User.builder()
                .id(dto.getUser().getId())
                .password(dto.getUser().getPassword())
                .build();

        ToDo toDo = ToDo.builder()
                .id(dto.getToDo().getId())
                .work(dto.getToDo().getWork())
                .date(dto.getToDo().getDate())
                .modifiedDate(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(schedulerService.updateToDo(user, toDo));
    }

    // 사용자와 해당 사용자의 모든 일정 삭제
    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteUser(
            @Valid @RequestBody UserRequestDto dto
    ) {

        User user = User.builder()
                .name(dto.getName())
                .id(dto.getId())
                .password(dto.getPassword())
                .build();

        schedulerService.deleteUser(user);

        return ResponseEntity.noContent().build();
    }

    // 해당 사용자의 특정 일정 삭제
    @DeleteMapping("/users/to-dos/{toDoId}")
    public ResponseEntity<Void> deleteToDo(
            @Min(value = 1, message = "ID 값은 1 이상이어야 합니다.") @PathVariable Long toDoId,
            @Valid @RequestBody UserRequestDto dto
    ) {

        User user = User.builder()
                .name(dto.getName())
                .id(dto.getId())
                .password(dto.getPassword())
                .build();

        schedulerService.deleteToDo(user, toDoId);

        return ResponseEntity.noContent().build();
    }
}
