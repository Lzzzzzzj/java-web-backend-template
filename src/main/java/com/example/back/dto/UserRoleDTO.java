package com.example.back.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserRoleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private List<Long> roleIds;
}
