package com.example.back.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "file.upload")
public class FileUploadProperties {

    private String path = "./uploads";

    private Long maxSize = 10485760L;

    private String allowedTypes = "jpg,jpeg,png,gif,bmp,webp,pdf,doc,docx,xls,xlsx";

    private String urlPrefix = "/api/files";
}
