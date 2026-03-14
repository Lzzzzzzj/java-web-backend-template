package com.example.back.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sys_role_permission")
public class RolePermission implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long roleId;
    private Long permissionId;
}
