package com.example.scheduler.dto;

import com.example.scheduler.entity.ToDo;
import com.example.scheduler.entity.User;
import lombok.Getter;

@Getter
public class ScheduleResponseDto {
    UserResponseDto user;
    ToDoResponseDto toDo;

    public ScheduleResponseDto(User user, ToDo toDo) {

        this.user = UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();

        this.toDo = ToDoResponseDto.builder()
                .id(toDo.getId())
                .name(user.getName())
                .email(user.getEmail())
                .date(toDo.getDate())
                .registeredDate(toDo.getRegisteredDate())
                .modifiedDate(toDo.getModifiedDate())
                .work(toDo.getWork())
                .build();
    }
}
