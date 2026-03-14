package com.example.back.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.back.common.PageResult;
import com.example.back.dto.FileQueryDTO;
import com.example.back.entity.SysFile;
import com.example.back.vo.FileVO;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;

public interface FileService extends IService<SysFile> {

    FileVO upload(MultipartFile file, String module, Long userId);

    FileVO getFileInfo(Long id);

    PageResult<FileVO> getPageList(FileQueryDTO queryDTO);

    void download(Long id, HttpServletResponse response);

    void deleteFile(Long id);
}
