package com.example.back.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class PermissionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "权限名称不能为空")
    @Size(max = 50, message = "权限名称长度不能超过50")
    private String permissionName;

    @NotBlank(message = "权限编码不能为空")
    @Size(max = 100, message = "权限编码长度不能超过100")
    private String permissionCode;

    @NotBlank(message = "资源类型不能为空")
    private String resourceType;

    @Size(max = 255, message = "资源路径长度不能超过255")
    private String resourcePath;

    private String method;

    private Long parentId;

    private Integer status;
    private Integer sort;
}
