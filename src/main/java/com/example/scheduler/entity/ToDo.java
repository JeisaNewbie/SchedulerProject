package com.example.scheduler.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class ToDo {
    private Long id;
    private Long userId;
    private LocalDateTime registeredDate;
    private LocalDateTime modifiedDate;
    private LocalDate date;
    private String work;
    @Setter
    private User user;
}
