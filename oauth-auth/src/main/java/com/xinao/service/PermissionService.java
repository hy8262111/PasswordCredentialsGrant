package com.xinao.service;
import com.xinao.entity.MenuVo;

import java.util.List;


public interface PermissionService {
    List<MenuVo> getPermissionsByRoleId(Integer roleId);
}
