package com.example.back.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PermissionVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String permissionName;
    private String permissionCode;
    private String resourceType;
    private String resourcePath;
    private String method;
    private Long parentId;
    private Integer status;
    private Integer sort;
    private LocalDateTime createTime;
    private List<PermissionVO> children;
}
