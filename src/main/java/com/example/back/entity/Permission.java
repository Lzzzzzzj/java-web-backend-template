package com.example.back.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.back.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_permission")
public class Permission extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String permissionName;
    private String permissionCode;
    private String resourceType;
    private String resourcePath;
    private String method;
    private Long parentId;
    private Integer status;
    private Integer sort;
}
