package com.lte.dao;

import com.lte.pojo.Permission;

import java.util.Set;

/**
 * @Auther: laite
 * @Date: 2021/8/15 - 08 - 15 - 15:54
 * @Description: com.lte.dao
 * @version: 1.0
 */
public interface PermissionDao {
    Set<Permission> findPermissionsByRoleId(Integer roleId);
}
