package com.example.back.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
}
