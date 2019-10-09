package com.xinao.mapper;

import com.xinao.entity.RoleVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface RcRoleMapper {
    @Select(value = "select role.* from role,user_role WHERE role.id = user_role.roleId and user_role.userId=#{userId}")
    List<RoleVo> getRoleByUserId(@Param("userId") Integer userId);
}