package com.example.scheduler.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ToDoRequestDto {
    private Long id;
    private LocalDate date;
    private String work;
}
