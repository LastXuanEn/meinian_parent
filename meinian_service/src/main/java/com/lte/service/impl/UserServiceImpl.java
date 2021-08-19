package com.lte.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lte.dao.UserDao;
import com.lte.pojo.User;
import com.lte.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Auther: laite
 * @Date: 2021/8/15 - 08 - 15 - 15:43
 * @Description: com.lte.service.impl
 * @version: 1.0
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Override
    public User findUserByUsername(String username) {
        User user = userDao.findUserByUsername(username);
        return user;
    }
}
