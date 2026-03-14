package com.example.back.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.back.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_file")
public class SysFile extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String fileName;
    private String originalFileName;
    private String filePath;
    private String fileUrl;
    private String fileType;
    private Long fileSize;
    private String fileMd5;
    private Long uploaderId;
    private String module;
    private Integer status;
}
