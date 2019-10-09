package com.xinao.service;
import com.xinao.entity.RoleVo;
import java.util.List;
public interface RoleService {
    List<RoleVo> getRoleByUserId(Integer userId);
}
