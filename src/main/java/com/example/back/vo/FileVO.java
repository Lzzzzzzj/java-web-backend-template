package com.example.back.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class FileVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String fileName;
    private String originalFileName;
    private String filePath;
    private String fileUrl;
    private String fileType;
    private Long fileSize;
    private String fileMd5;
    private Long uploaderId;
    private String uploaderName;
    private String module;
    private Integer status;
    private LocalDateTime createTime;
}
