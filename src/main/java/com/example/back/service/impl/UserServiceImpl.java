package com.example.back.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.back.common.BusinessException;
import com.example.back.common.PageResult;
import com.example.back.common.ResultCode;
import com.example.back.dto.LoginDTO;
import com.example.back.dto.PageDTO;
import com.example.back.dto.UserDTO;
import com.example.back.entity.User;
import com.example.back.entity.UserRole;
import com.example.back.mapper.UserMapper;
import com.example.back.mapper.UserRoleMapper;
import com.example.back.security.JwtTokenProvider;
import com.example.back.service.UserService;
import com.example.back.vo.TokenVO;
import com.example.back.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRoleMapper userRoleMapper;

    private static final String TOKEN_PREFIX = "token:";
    private static final String USER_INFO_PREFIX = "user:info:";
    private static final long TOKEN_EXPIRE_TIME = 7200L;

    @Override
    public TokenVO login(LoginDTO loginDTO) {
        log.info("用户登录：{}", loginDTO.getUsername());
        
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = this.getOne(wrapper);
        
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        if (user.getStatus() != 1) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }
        
        if (!BCrypt.checkpw(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }
        
        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), user.getUsername());
        
        try {
            String tokenKey = TOKEN_PREFIX + user.getId();
            redisTemplate.opsForValue().set(tokenKey, accessToken, TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
            log.info("Token已存入Redis，key: {}, token前缀: {}", tokenKey, accessToken.substring(0, Math.min(20, accessToken.length())));
            
            String savedToken = (String) redisTemplate.opsForValue().get(tokenKey);
            log.info("验证Redis存储，savedToken: {}", savedToken != null ? "存在" : "不存在");
        } catch (Exception e) {
            log.error("Token存入Redis失败: {}", e.getMessage(), e);
        }
        
        log.info("用户登录成功：{}", user.getUsername());
        return new TokenVO(accessToken, refreshToken, TOKEN_EXPIRE_TIME);
    }

    @Override
    public TokenVO refreshToken(String refreshToken) {
        log.info("刷新Token");
        
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }
        
        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        
        String accessToken = jwtTokenProvider.generateAccessToken(userId, username);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(userId, username);
        
        try {
            redisTemplate.opsForValue().set(TOKEN_PREFIX + userId, accessToken, TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
            log.info("刷新Token已存入Redis，userId: {}", userId);
        } catch (Exception e) {
            log.error("刷新Token存入Redis失败: {}", e.getMessage());
        }
        
        return new TokenVO(accessToken, newRefreshToken, TOKEN_EXPIRE_TIME);
    }

    @Override
    public UserVO getUserInfo(Long userId) {
        String cacheKey = USER_INFO_PREFIX + userId;
        try {
            UserVO cachedUserVO = (UserVO) redisTemplate.opsForValue().get(cacheKey);
            if (cachedUserVO != null) {
                return cachedUserVO;
            }
        } catch (Exception e) {
            log.warn("从Redis获取用户缓存失败: {}", e.getMessage());
        }
        
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        
        List<String> roles = baseMapper.selectRolesByUserId(userId);
        List<String> permissions = baseMapper.selectPermissionsByUserId(userId);
        userVO.setRoles(roles);
        userVO.setPermissions(permissions);
        
        try {
            redisTemplate.opsForValue().set(cacheKey, userVO, 30, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.warn("用户信息存入Redis失败: {}", e.getMessage());
        }
        
        return userVO;
    }

    @Override
    public PageResult<UserVO> getPageList(PageDTO pageDTO) {
        log.info("分页查询用户列表：current={}, size={}", pageDTO.getCurrent(), pageDTO.getSize());
        
        Page<User> page = new Page<>(pageDTO.getCurrent(), pageDTO.getSize());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        
        if (StrUtil.isNotBlank(pageDTO.getKeyword())) {
            wrapper.like(User::getUsername, pageDTO.getKeyword())
                    .or()
                    .like(User::getNickname, pageDTO.getKeyword())
                    .or()
                    .like(User::getPhone, pageDTO.getKeyword());
        }
        
        wrapper.orderByDesc(User::getCreateTime);
        
        Page<User> userPage = this.page(page, wrapper);
        
        List<UserVO> userVOList = userPage.getRecords().stream()
                .map(user -> BeanUtil.copyProperties(user, UserVO.class))
                .toList();
        
        return new PageResult<>(userVOList, userPage.getTotal(), userPage.getSize(), userPage.getCurrent());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addUser(UserDTO userDTO) {
        log.info("新增用户：{}", userDTO.getUsername());
        
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userDTO.getUsername());
        if (this.count(wrapper) > 0) {
            throw new BusinessException(ResultCode.USER_EXISTS);
        }
        
        User user = BeanUtil.copyProperties(userDTO, User.class);
        user.setPassword(BCrypt.hashpw(userDTO.getPassword()));
        user.setStatus(1);
        user.setDeleted(0);
        
        this.save(user);
        
        log.info("新增用户成功：{}", user.getId());
        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserDTO userDTO) {
        log.info("更新用户：{}", userDTO.getId());
        
        User user = this.getById(userDTO.getId());
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        BeanUtil.copyProperties(userDTO, user, "id", "password", "createTime");
        
        if (StrUtil.isNotBlank(userDTO.getPassword())) {
            user.setPassword(BCrypt.hashpw(userDTO.getPassword()));
        }
        
        this.updateById(user);
        
        try {
            redisTemplate.delete(USER_INFO_PREFIX + user.getId());
        } catch (Exception e) {
            log.warn("删除Redis缓存失败: {}", e.getMessage());
        }
        
        log.info("更新用户成功：{}", user.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        log.info("删除用户：{}", id);
        
        User user = this.getById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        user.setDeleted(1);
        this.updateById(user);
        
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, id);
        userRoleMapper.delete(wrapper);
        
        try {
            redisTemplate.delete(USER_INFO_PREFIX + id);
            redisTemplate.delete(TOKEN_PREFIX + id);
        } catch (Exception e) {
            log.warn("删除Redis缓存失败: {}", e.getMessage());
        }
        
        log.info("删除用户成功：{}", id);
    }

    @Override
    public void updateUserStatus(Long id, Integer status) {
        log.info("更新用户状态：id={}, status={}", id, status);
        
        User user = this.getById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        user.setStatus(status);
        this.updateById(user);
        
        try {
            redisTemplate.delete(USER_INFO_PREFIX + id);
        } catch (Exception e) {
            log.warn("删除Redis缓存失败: {}", e.getMessage());
        }
        
        log.info("更新用户状态成功：{}", id);
    }
}
