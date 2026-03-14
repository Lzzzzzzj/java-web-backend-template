package com.example.back.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class FileUploadDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String module;

    @NotBlank(message = "文件类型不能为空")
    private String fileType;
}
