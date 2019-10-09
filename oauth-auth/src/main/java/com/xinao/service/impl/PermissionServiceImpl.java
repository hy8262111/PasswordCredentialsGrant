package com.xinao.service.impl;

import com.xinao.entity.MenuVo;
import com.xinao.mapper.RcMenuMapper;
import com.xinao.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private RcMenuMapper menuMapper;
    @Override
    public List<MenuVo> getPermissionsByRoleId(Integer roleId) {
        return menuMapper.getPermissionsByRoleId(roleId);
    }
}
