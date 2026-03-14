package com.example.back.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sys_user_role")
public class UserRole implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private Long roleId;
}
