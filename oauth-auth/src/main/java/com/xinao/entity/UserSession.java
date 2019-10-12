package com.xinao.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @Author: houyong
 * @Date: 2019/10/10 16:22
 * @describe
 */
@Data
public class UserSession extends User {
    private String userId;
    private String account;
    private String name;
    private String tenantId;
    private List<String> lists;
    private Set<String> sets;
    private String groups;

    public UserSession(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, true, true, true, true, authorities);
    }
}
