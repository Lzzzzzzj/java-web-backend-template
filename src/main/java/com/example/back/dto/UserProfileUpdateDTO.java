package com.example.back.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserProfileUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Size(max = 50, message = "昵称长度不能超过50")
    private String nickname;

    @Email(message = "邮箱格式不正确")
    private String email;

    @Size(max = 20, message = "手机号长度不能超过20")
    private String phone;

    @Size(max = 255, message = "头像URL长度不能超过255")
    private String avatar;
}
