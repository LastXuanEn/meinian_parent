package com.lte.service;

import com.lte.pojo.User;

/**
 * @Auther: laite
 * @Date: 2021/8/15 - 08 - 15 - 15:42
 * @Description: com.lte.service
 * @version: 1.0
 */
public interface UserService {
    User findUserByUsername(String username);
}
