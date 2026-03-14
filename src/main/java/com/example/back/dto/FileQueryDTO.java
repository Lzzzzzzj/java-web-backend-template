package com.example.back.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class FileQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long current = 1L;
    private Long size = 10L;
    private String fileName;
    private String fileType;
    private String module;
    private Long uploaderId;
}
