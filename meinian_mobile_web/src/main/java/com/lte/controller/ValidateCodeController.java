package com.lte.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lte.constant.MessageConstant;
import com.lte.constant.RedisMessageConstant;
import com.lte.entity.Result;
import com.lte.util.SMSUtils;
import com.lte.util.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @Auther: laite
 * @Date: 2021/8/12 - 08 - 12 - 23:09
 * @Description: com.lte.controller
 * @version: 1.0
 */
@RequestMapping("/validateCode")
@RestController //ResponseBody+Controller
public class ValidateCodeController {
    @Autowired
    JedisPool jedisPool;
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        //1-调用验证码生成工具类，随机生成4位数的验证码
        Integer integer = ValidateCodeUtils.generateValidateCode(4);
        String code = integer.toString(integer);
       /* //2-发送短信，调用SMS工具类
        try {
            SMSUtils.sendShortMessage(telephone,code);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }*/
        //将生成的验证码缓存到redis中
        jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_ORDER,50*60,code);
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS,code);
    }

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        try {
            //1-调用验证码生成工具类，随机生成4位数的验证码
            Integer integer = ValidateCodeUtils.generateValidateCode(4);
            String code = Integer.toString(integer);
            //将生成的验证码缓存到redis中
            jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_LOGIN,50*60,code);
            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
}
