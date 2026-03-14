package com.example.back.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.back.dto.LoginDTO;
import com.example.back.dto.PageDTO;
import com.example.back.dto.UserDTO;
import com.example.back.entity.User;
import com.example.back.common.PageResult;
import com.example.back.vo.TokenVO;
import com.example.back.vo.UserVO;

public interface UserService extends IService<User> {

    TokenVO login(LoginDTO loginDTO);

    TokenVO refreshToken(String refreshToken);

    UserVO getUserInfo(Long userId);

    PageResult<UserVO> getPageList(PageDTO pageDTO);

    Long addUser(UserDTO userDTO);

    void updateUser(UserDTO userDTO);

    void deleteUser(Long id);

    void updateUserStatus(Long id, Integer status);
}
