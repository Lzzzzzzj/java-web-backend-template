package com.example.back.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String path;
    private String icon;
    private Long parentId;
    private Integer sort;
    private List<MenuVO> children;
}
