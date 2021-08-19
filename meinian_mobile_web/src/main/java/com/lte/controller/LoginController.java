package com.lte.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lte.constant.MessageConstant;
import com.lte.constant.RedisMessageConstant;
import com.lte.entity.Result;
import com.lte.pojo.Member;
import com.lte.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @Auther: laite
 * @Date: 2021/8/14 - 08 - 14 - 10:51
 * @Description: com.lte.controller
 * @version: 1.0
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Reference
    MemberService memberService;

    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/check")
    public Result check(HttpServletResponse response, @RequestBody Map map){
        String telephone = (String) map.get("telephone");
        String validateCode =(String)map.get("validateCode");
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if(codeInRedis ==null || !codeInRedis.equals(validateCode)){
            //验证码为空，或者验证码错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }else {
            //判断是否属于会员
          Member member =  memberService.findByTelephone(telephone);
            if(member==null){
                //当前用户不是会员，自动完成注册
                member = new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberService.add(member);

            }
            //写入Cookie，跟踪用户
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            cookie.setPath("/");//路径
            cookie.setMaxAge(60*60*24*30);//有效期30天（单位秒）
            response.addCookie(cookie);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }
    }
}
