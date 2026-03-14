package com.example.back.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.back.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class Role extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String roleName;
    private String roleCode;
    private String description;
    private Integer status;
    private Integer sort;
}
