package com.example.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ToDoResponseDto {
    private Long id;
    private String name;
    private String email;
    private LocalDate date;
    private LocalDateTime registeredDate;
    private LocalDateTime modifiedDate;
    private String work;
}
