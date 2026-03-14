package com.example.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.back.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<String> selectRolesByUserId(@Param("userId") Long userId);

    List<String> selectPermissionsByUserId(@Param("userId") Long userId);
}
