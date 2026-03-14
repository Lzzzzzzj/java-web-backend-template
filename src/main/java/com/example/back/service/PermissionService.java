package com.example.back.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.back.entity.Permission;

import java.util.List;

public interface PermissionService extends IService<Permission> {

    List<Permission> getPermissionsByUserId(Long userId);
}
