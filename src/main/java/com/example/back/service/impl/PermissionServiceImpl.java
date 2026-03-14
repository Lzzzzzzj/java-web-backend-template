package com.example.back.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.back.entity.Permission;
import com.example.back.entity.RolePermission;
import com.example.back.entity.UserRole;
import com.example.back.mapper.PermissionMapper;
import com.example.back.mapper.RolePermissionMapper;
import com.example.back.mapper.UserRoleMapper;
import com.example.back.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    private final UserRoleMapper userRoleMapper;
    private final RolePermissionMapper rolePermissionMapper;

    @Override
    public List<Permission> getPermissionsByUserId(Long userId) {
        log.info("查询用户权限：userId={}", userId);
        
        LambdaQueryWrapper<UserRole> urWrapper = new LambdaQueryWrapper<>();
        urWrapper.eq(UserRole::getUserId, userId);
        List<UserRole> userRoles = userRoleMapper.selectList(urWrapper);
        
        if (userRoles.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Long> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .toList();
        
        LambdaQueryWrapper<RolePermission> rpWrapper = new LambdaQueryWrapper<>();
        rpWrapper.in(RolePermission::getRoleId, roleIds);
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(rpWrapper);
        
        if (rolePermissions.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Long> permissionIds = rolePermissions.stream()
                .map(RolePermission::getPermissionId)
                .distinct()
                .toList();
        
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Permission::getId, permissionIds)
                .eq(Permission::getStatus, 1)
                .eq(Permission::getDeleted, 0);
        
        return this.list(wrapper);
    }
}
