package com.example.back.controller;

import com.example.back.common.PageResult;
import com.example.back.common.Result;
import com.example.back.dto.PageDTO;
import com.example.back.dto.UserDTO;
import com.example.back.security.LoginUser;
import com.example.back.service.UserService;
import com.example.back.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    public Result<UserVO> getUserInfo(@AuthenticationPrincipal LoginUser loginUser) {
        log.info("获取用户信息：userId={}", loginUser.getUserId());
        UserVO userVO = userService.getUserInfo(loginUser.getUserId());
        return Result.success(userVO);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询用户列表", description = "分页查询用户列表信息")
    public Result<PageResult<UserVO>> getPageList(PageDTO pageDTO) {
        log.info("分页查询用户列表");
        PageResult<UserVO> pageResult = userService.getPageList(pageDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情", description = "根据ID获取用户详细信息")
    public Result<UserVO> getUserById(@PathVariable Long id) {
        log.info("获取用户详情：id={}", id);
        UserVO userVO = userService.getUserInfo(id);
        return Result.success(userVO);
    }

    @PostMapping
    @Operation(summary = "新增用户", description = "创建新用户")
    public Result<Long> addUser(@Valid @RequestBody UserDTO userDTO) {
        log.info("新增用户：{}", userDTO.getUsername());
        Long userId = userService.addUser(userDTO);
        return Result.success(userId);
    }

    @PutMapping
    @Operation(summary = "更新用户", description = "更新用户信息")
    public Result<Void> updateUser(@Valid @RequestBody UserDTO userDTO) {
        log.info("更新用户：id={}", userDTO.getId());
        userService.updateUser(userDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "根据ID删除用户")
    public Result<Void> deleteUser(@PathVariable Long id) {
        log.info("删除用户：id={}", id);
        userService.deleteUser(id);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新用户状态", description = "启用或禁用用户")
    public Result<Void> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        log.info("更新用户状态：id={}, status={}", id, status);
        userService.updateUserStatus(id, status);
        return Result.success();
    }
}
