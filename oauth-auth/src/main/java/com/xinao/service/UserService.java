package com.xinao.service;

import com.xinao.entity.UserVo;

public interface UserService {
    UserVo findByUsername(String username);
}
