package com.lte.dao;

import com.lte.pojo.Role;

import java.util.Set;

/**
 * @Auther: laite
 * @Date: 2021/8/15 - 08 - 15 - 15:51
 * @Description: com.lte.dao
 * @version: 1.0
 */
public interface RoleDao {
    Set<Role> findRolesByUserId(Integer userId);

}
