package com.example.back.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.back.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class User extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String avatar;
    private Integer status;
    private Long deptId;
}
