package com.example.back.controller;

import com.example.back.common.Result;
import com.example.back.dto.LoginDTO;
import com.example.back.service.UserService;
import com.example.back.vo.TokenVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "认证相关接口")
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录获取Token")
    public Result<TokenVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        log.info("用户登录请求：{}", loginDTO.getUsername());
        TokenVO tokenVO = userService.login(loginDTO);
        return Result.success(tokenVO);
    }

    @PostMapping("/refresh")
    @Operation(summary = "刷新Token", description = "使用RefreshToken刷新AccessToken")
    public Result<TokenVO> refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
        log.info("刷新Token请求");
        TokenVO tokenVO = userService.refreshToken(refreshToken);
        return Result.success(tokenVO);
    }

    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户登出，清除Token")
    public Result<Void> logout() {
        log.info("用户登出请求");
        return Result.success();
    }
}
