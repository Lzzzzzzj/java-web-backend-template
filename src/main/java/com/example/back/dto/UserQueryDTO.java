package com.example.back.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long current = 1L;
    private Long size = 10L;
    private String username;
    private String nickname;
    private String phone;
    private String email;
    private Integer status;
    private Long deptId;
    private LocalDateTime createTimeStart;
    private LocalDateTime createTimeEnd;
}
