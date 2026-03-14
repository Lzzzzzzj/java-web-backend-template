package com.example.back.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.back.entity.Role;
import com.example.back.entity.UserRole;
import com.example.back.mapper.RoleMapper;
import com.example.back.mapper.UserRoleMapper;
import com.example.back.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final UserRoleMapper userRoleMapper;

    @Override
    public List<Role> getRolesByUserId(Long userId) {
        log.info("查询用户角色：userId={}", userId);
        
        LambdaQueryWrapper<UserRole> urWrapper = new LambdaQueryWrapper<>();
        urWrapper.eq(UserRole::getUserId, userId);
        List<UserRole> userRoles = userRoleMapper.selectList(urWrapper);
        
        if (userRoles.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Long> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .toList();
        
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Role::getId, roleIds)
                .eq(Role::getStatus, 1)
                .eq(Role::getDeleted, 0);
        
        return this.list(wrapper);
    }
}
