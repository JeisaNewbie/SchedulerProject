package com.example.scheduler.dto.response;

import com.example.scheduler.entity.ToDo;
import com.example.scheduler.entity.User;
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


    public ToDoResponseDto(ToDo toDo, User user) {
        this.id = toDo.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.date = toDo.getDate();
        this.registeredDate = toDo.getRegisteredDate();
        this.modifiedDate = toDo.getModifiedDate();
        this.work = toDo.getWork();
    }

}
