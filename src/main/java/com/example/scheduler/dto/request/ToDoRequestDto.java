package com.example.scheduler.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ToDoRequestDto {
    @Min(value = 1, message = "ID 값은 1 이상이어야 합니다.")
    private Long id;

    @NotNull (message = "일정을 적어주세요.")
    private LocalDate date;

    @NotBlank (message = "할일을 적어주세요.")
    @Size(min = 1, max = 200)
    private String work;
}
