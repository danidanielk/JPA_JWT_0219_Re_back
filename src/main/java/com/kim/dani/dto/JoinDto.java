package com.kim.dani.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
public class JoinDto {


    @NotBlank(message = "이름을 입력해주세요")
    @Size(min = 1, max = 10, message = "이름은 1~10자")
    private String name;

    @NotBlank(message = "휴대폰 번호를 입력해주세요")
    private String phone;

    @NotBlank(message = "이메일을 입력해주세요")
    @Email
    private String email;

    @NotBlank(message = "패스워드를 입력해주세요")
    @Pattern(regexp = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$")
    @Size(min = 8,max = 15,message = "8~15자 이내")
    private String password;

}
