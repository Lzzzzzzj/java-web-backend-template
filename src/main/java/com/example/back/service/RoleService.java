package com.example.back.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.back.entity.Role;

import java.util.List;

public interface RoleService extends IService<Role> {

    List<Role> getRolesByUserId(Long userId);
}
