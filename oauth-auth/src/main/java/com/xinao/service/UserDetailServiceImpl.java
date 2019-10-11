package com.xinao.service;
import com.alibaba.fastjson.JSON;
import com.xinao.entity.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: houyong
 * @Date: 2019/9/26 10:25
 * @describe
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private PermissionService permissionService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserVo userVo = userService.findByUsername(username);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        List<RoleVo> roleVoList = roleService.getRoleByUserId(userVo.getId());
        for (RoleVo role : roleVoList) {
            //角色必须是ROLE_开头，可以在数据库中设置
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + role.getValue());
            grantedAuthorities.add(grantedAuthority);
            List<MenuVo> permissionList = permissionService.getPermissionsByRoleId(role.getId());
            for (MenuVo menu : permissionList) {
                GrantedAuthority authority = new SimpleGrantedAuthority(menu.getCode());
                grantedAuthorities.add(authority);
            }
        }
       /* User user = new User(userVo.getUsername(), userVo.getPassword(),
                true, true, true, true, grantedAuthorities);
        return user;
*/
        /**
         * 扩展用户信息，放入redis中，后期根据需要
         */

        UserSession userSession = new UserSession(userVo.getUsername(), userVo.getPassword(), grantedAuthorities);
        userSession.setTenantId("aaa");
        userSession.setUserId("123456,12,3,23");
        List<String> lists = new ArrayList<>();
        lists.add("aaa");
        lists.add("aaa");
        lists.add("aaa");
        userSession.setLists(lists);

        Set<String> sets = new HashSet<>();
        sets.add("bbb");
        sets.add("bbb");
        sets.add("bbb");
        userSession.setSets(sets);

        return userSession;
    }

}
