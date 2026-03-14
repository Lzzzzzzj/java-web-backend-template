package com.example.back.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class LoginLogVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private String username;
    private String ip;
    private String location;
    private String browser;
    private String os;
    private Integer status;
    private String message;
    private LocalDateTime loginTime;
}
