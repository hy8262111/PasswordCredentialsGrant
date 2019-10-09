package com.xinao.service.impl;

import com.xinao.entity.RoleVo;
import com.xinao.mapper.RcRoleMapper;
import com.xinao.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RcRoleMapper roleMapper;

    @Override
    public List<RoleVo> getRoleByUserId(Integer userId) {
        return roleMapper.getRoleByUserId(userId);
    }
}
