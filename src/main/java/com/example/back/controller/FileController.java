package com.example.back.controller;

import com.example.back.common.PageResult;
import com.example.back.common.Result;
import com.example.back.dto.FileQueryDTO;
import com.example.back.security.LoginUser;
import com.example.back.service.FileService;
import com.example.back.vo.FileVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Tag(name = "文件管理", description = "文件上传下载相关接口")
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    @Operation(summary = "上传文件", description = "上传单个文件")
    public Result<FileVO> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "module", required = false) String module,
            @AuthenticationPrincipal LoginUser loginUser) {
        log.info("上传文件：{}, module: {}", file.getOriginalFilename(), module);
        FileVO fileVO = fileService.upload(file, module, loginUser.getUserId());
        return Result.success(fileVO);
    }

    @PostMapping("/upload/batch")
    @Operation(summary = "批量上传文件", description = "批量上传多个文件")
    public Result<java.util.List<FileVO>> uploadBatch(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "module", required = false) String module,
            @AuthenticationPrincipal LoginUser loginUser) {
        log.info("批量上传文件：{} 个", files.length);
        java.util.List<FileVO> fileVOList = new java.util.ArrayList<>();
        for (MultipartFile file : files) {
            FileVO fileVO = fileService.upload(file, module, loginUser.getUserId());
            fileVOList.add(fileVO);
        }
        return Result.success(fileVOList);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取文件信息", description = "根据ID获取文件详细信息")
    public Result<FileVO> getFileInfo(@PathVariable Long id) {
        log.info("获取文件信息：id={}", id);
        FileVO fileVO = fileService.getFileInfo(id);
        return Result.success(fileVO);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询文件列表", description = "分页查询文件列表信息")
    public Result<PageResult<FileVO>> getPageList(FileQueryDTO queryDTO) {
        log.info("分页查询文件列表");
        PageResult<FileVO> pageResult = fileService.getPageList(queryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/download/{id}")
    @Operation(summary = "下载文件", description = "根据ID下载文件")
    public void download(@PathVariable Long id, HttpServletResponse response) {
        log.info("下载文件：id={}", id);
        fileService.download(id, response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文件", description = "根据ID删除文件")
    public Result<Void> deleteFile(@PathVariable Long id) {
        log.info("删除文件：id={}", id);
        fileService.deleteFile(id);
        return Result.success();
    }
}
