package com.example.back.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class LoginUser extends User {

    private static final long serialVersionUID = 1L;

    private final Long userId;
    private final String nickname;
    private final String email;
    private final String phone;

    public LoginUser(Long userId, String username, String password, String nickname, 
                     String email, String phone, 
                     Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
    }
}
