package com.example.scheduler.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class UserRequestDto {
    @Min(value = 1, message = "ID 값은 1 이상이어야 합니다.")
    private Long id;

    @NotBlank(message = "이름을 적어주세요.")
    private String name;

    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "이메일 형식을 지켜주세요.")
    private String email;

    @NotBlank (message = "비밀번호를 적어주세요.")
    private String password;

}
