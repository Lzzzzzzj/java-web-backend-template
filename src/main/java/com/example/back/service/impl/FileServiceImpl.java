package com.example.back.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.back.common.BusinessException;
import com.example.back.common.PageResult;
import com.example.back.common.ResultCode;
import com.example.back.config.FileUploadProperties;
import com.example.back.dto.FileQueryDTO;
import com.example.back.entity.SysFile;
import com.example.back.entity.User;
import com.example.back.mapper.SysFileMapper;
import com.example.back.mapper.UserMapper;
import com.example.back.service.FileService;
import com.example.back.vo.FileVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements FileService {

    private final FileUploadProperties fileUploadProperties;
    private final UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileVO upload(MultipartFile file, String module, Long userId) {
        log.info("上传文件：{}, module: {}, userId: {}", file.getOriginalFilename(), module, userId);
        
        if (file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }
        
        String originalFileName = file.getOriginalFilename();
        String fileExtension = FileUtil.extName(originalFileName);
        
        if (!isAllowedType(fileExtension)) {
            throw new BusinessException("不支持的文件类型: " + fileExtension);
        }
        
        if (file.getSize() > fileUploadProperties.getMaxSize()) {
            throw new BusinessException("文件大小超过限制: " + fileUploadProperties.getMaxSize() / 1024 / 1024 + "MB");
        }
        
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String newFileName = IdUtil.fastSimpleUUID() + "." + fileExtension;
        String relativePath = StrUtil.isBlank(module) 
                ? datePath + "/" + newFileName 
                : module + "/" + datePath + "/" + newFileName;
        
        Path uploadPath = Paths.get(fileUploadProperties.getPath(), relativePath);
        
        try {
            Files.createDirectories(uploadPath.getParent());
            file.transferTo(uploadPath.toFile());
            log.info("文件保存成功：{}", uploadPath);
        } catch (IOException e) {
            log.error("文件保存失败", e);
            throw new BusinessException("文件保存失败");
        }
        
        String fileMd5;
        try {
            fileMd5 = DigestUtil.md5Hex(file.getInputStream());
        } catch (IOException e) {
            log.error("计算文件MD5失败", e);
            fileMd5 = "";
        }
        
        SysFile sysFile = new SysFile();
        sysFile.setFileName(newFileName);
        sysFile.setOriginalFileName(originalFileName);
        sysFile.setFilePath(uploadPath.toString());
        sysFile.setFileUrl(fileUploadProperties.getUrlPrefix() + "/" + relativePath);
        sysFile.setFileType(fileExtension.toLowerCase());
        sysFile.setFileSize(file.getSize());
        sysFile.setFileMd5(fileMd5);
        sysFile.setUploaderId(userId);
        sysFile.setModule(module);
        sysFile.setStatus(1);
        sysFile.setDeleted(0);
        
        this.save(sysFile);
        
        log.info("文件上传成功：id={}", sysFile.getId());
        return convertToVO(sysFile);
    }

    @Override
    public FileVO getFileInfo(Long id) {
        SysFile sysFile = super.getById(id);
        if (sysFile == null || sysFile.getDeleted() == 1) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return convertToVO(sysFile);
    }

    @Override
    public PageResult<FileVO> getPageList(FileQueryDTO queryDTO) {
        log.info("分页查询文件列表");
        
        Page<SysFile> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        LambdaQueryWrapper<SysFile> wrapper = new LambdaQueryWrapper<>();
        
        if (StrUtil.isNotBlank(queryDTO.getFileName())) {
            wrapper.like(SysFile::getOriginalFileName, queryDTO.getFileName());
        }
        if (StrUtil.isNotBlank(queryDTO.getFileType())) {
            wrapper.eq(SysFile::getFileType, queryDTO.getFileType());
        }
        if (StrUtil.isNotBlank(queryDTO.getModule())) {
            wrapper.eq(SysFile::getModule, queryDTO.getModule());
        }
        if (queryDTO.getUploaderId() != null) {
            wrapper.eq(SysFile::getUploaderId, queryDTO.getUploaderId());
        }
        
        wrapper.eq(SysFile::getDeleted, 0)
                .orderByDesc(SysFile::getCreateTime);
        
        Page<SysFile> filePage = this.page(page, wrapper);
        
        List<FileVO> fileVOList = filePage.getRecords().stream()
                .map(this::convertToVO)
                .toList();
        
        return new PageResult<>(fileVOList, filePage.getTotal(), filePage.getSize(), filePage.getCurrent());
    }

    @Override
    public void download(Long id, HttpServletResponse response) {
        log.info("下载文件：id={}", id);
        
        SysFile sysFile = super.getById(id);
        if (sysFile == null || sysFile.getDeleted() == 1) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        
        Path filePath = Paths.get(sysFile.getFilePath());
        if (!Files.exists(filePath)) {
            throw new BusinessException("文件不存在");
        }
        
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + 
                URLEncoder.encode(sysFile.getOriginalFileName(), StandardCharsets.UTF_8));
        response.setHeader("Content-Length", String.valueOf(sysFile.getFileSize()));
        
        try (OutputStream os = response.getOutputStream()) {
            Files.copy(filePath, os);
            os.flush();
        } catch (IOException e) {
            log.error("文件下载失败", e);
            throw new BusinessException("文件下载失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFile(Long id) {
        log.info("删除文件：id={}", id);
        
        SysFile sysFile = super.getById(id);
        if (sysFile == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        
        Path filePath = Paths.get(sysFile.getFilePath());
        try {
            Files.deleteIfExists(filePath);
            log.info("物理文件删除成功：{}", filePath);
        } catch (IOException e) {
            log.warn("物理文件删除失败：{}", filePath, e);
        }
        
        sysFile.setDeleted(1);
        this.updateById(sysFile);
        
        log.info("文件记录删除成功：id={}", id);
    }

    private boolean isAllowedType(String fileExtension) {
        if (StrUtil.isBlank(fileExtension)) {
            return false;
        }
        String allowedTypes = fileUploadProperties.getAllowedTypes();
        List<String> allowedList = Arrays.asList(allowedTypes.toLowerCase().split(","));
        return allowedList.contains(fileExtension.toLowerCase());
    }

    private FileVO convertToVO(SysFile sysFile) {
        FileVO fileVO = BeanUtil.copyProperties(sysFile, FileVO.class);
        
        if (sysFile.getUploaderId() != null) {
            User user = userMapper.selectById(sysFile.getUploaderId());
            if (user != null) {
                fileVO.setUploaderName(user.getNickname() != null ? user.getNickname() : user.getUsername());
            }
        }
        
        return fileVO;
    }
}
