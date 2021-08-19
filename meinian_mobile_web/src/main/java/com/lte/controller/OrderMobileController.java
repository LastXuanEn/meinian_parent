package com.lte.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lte.constant.MessageConstant;
import com.lte.constant.RedisMessageConstant;
import com.lte.entity.Result;
import com.lte.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @Auther: laite
 * @Date: 2021/8/13 - 08 - 13 - 14:07
 * @Description: com.lte.controller
 * @version: 1.0
 */
@RestController
@RequestMapping("/order")
public class OrderMobileController {
    @Autowired
    JedisPool jedisPool;

    @Reference
    OrderService orderService;

    @RequestMapping("/saveOrder")
    public Result saveOrder(@RequestBody Map map){
        //1-页面获取手机号码
       String telephone = (String)map.get("telephone");
       //2-页面获取验证码
       String validateCode = (String)map.get("validateCode");
       //3-从redis缓存数据库里面获取验证码
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
       //4-校验验证码
        if(codeInRedis == null || !codeInRedis.equals(validateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        try {
            //4-调用旅游预约服务
            Result result = orderService.order(map);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDER_FAIL);
        }
    }

    /**
     * @Description
     * <p>会员姓名：{{orderInfo.member}}</p>
     * <p>旅游套餐：{{orderInfo.setmeal}}</p>
     * <p>旅游日期：{{orderInfo.orderDate}}</p>
     * <p>预约类型：{{orderInfo.orderType}}</p>
     * 通过orderID做三表连接查询到上述的信息
     * @Param
     * @return
     **/
    @RequestMapping("/findById")
    public Result  findById(Integer id){
        try {
            Map<String,Object> map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }

    }
}
