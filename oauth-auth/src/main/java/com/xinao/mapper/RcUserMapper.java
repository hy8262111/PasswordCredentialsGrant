package com.xinao.mapper;
import com.xinao.entity.UserVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@org.apache.ibatis.annotations.Mapper
public interface RcUserMapper {
    @Select("select * from user where username=#{username}")
    UserVo findByUserName(@Param("username") String userName);

}