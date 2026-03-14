package com.example.back.security;

import com.example.back.entity.User;
import com.example.back.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("加载用户信息：{}", username);
        
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User> wrapper = 
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);
        
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        
        List<String> roles = userMapper.selectRolesByUserId(user.getId());
        List<String> permissions = userMapper.selectPermissionsByUserId(user.getId());
        
        List<SimpleGrantedAuthority> authorities = permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
        
        return new LoginUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getNickname(),
                user.getEmail(),
                user.getPhone(),
                authorities
        );
    }
}
