package com.example.scheduler.dto.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleRequestDto {
    @Valid
    UserRequestDto user;

    @Valid
    ToDoRequestDto toDo;
}
