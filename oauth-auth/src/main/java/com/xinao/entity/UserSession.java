package com.xinao.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

/**
 * @Author: houyong
 * @Date: 2019/10/10 16:22
 * @describe
 */
public class UserSession extends User {
    private String userId;
    private String account;
    private String Name;
    private String TenantId;
    private List<String> Groups;
    public UserSession(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, true, true, true, true, authorities);
    }
}
