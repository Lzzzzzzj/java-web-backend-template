package com.example.back.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private String avatar;
    private Integer status;
    private Long deptId;
    private List<RoleVO> roles;
    private List<String> permissions;
}
