package com.example.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class UserRequestDto {
    private Long id;
    private String name;
    private String email;
    private String password;

}
