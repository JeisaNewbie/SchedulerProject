package com.example.scheduler.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class Schedule {
    private LocalDate date;
    User user;
    ToDo toDo;
}
