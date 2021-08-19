package com.lte.controller;

import com.lte.constant.MessageConstant;
import com.lte.entity.Result;
import com.lte.service.UserService;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: laite
 * @Date: 2021/8/15 - 08 - 15 - 16:26
 * @Description: com.lte.controller
 * @version: 1.0
 */
@RestController //ResponseBody+controller
@RequestMapping("/user")
public class UserController {
    @Reference
    UserService userService;
    //获取当前登录用户的用户名
    @RequestMapping("/getUsername")
    public Result getUsername()throws Exception{
        try{
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user);
        }catch (Exception e){
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }

}
