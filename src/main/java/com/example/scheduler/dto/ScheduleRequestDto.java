package com.example.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ScheduleRequestDto {
    private String name;
    private String email;
    private String password;
    private String work;
    private LocalDate date;
}
