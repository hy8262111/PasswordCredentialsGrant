package com.xinao.service.impl;

import com.xinao.entity.UserVo;
import com.xinao.mapper.RcUserMapper;
import com.xinao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RcUserMapper userMapper;

    @Override
    public UserVo findByUsername(String username) {
        return userMapper.findByUserName(username);
    }
}
