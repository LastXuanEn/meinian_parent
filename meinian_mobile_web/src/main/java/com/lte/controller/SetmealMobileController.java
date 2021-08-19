package com.lte.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lte.constant.MessageConstant;
import com.lte.entity.Result;
import com.lte.pojo.Setmeal;
import com.lte.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * @Auther: laite
 * @Date: 2021/8/12 - 08 - 12 - 14:19
 * @Description: com.lte.controller
 * @version: 1.0
 */
@RestController  //ResponseBody and Contorller
@RequestMapping("/setmeal")
public class SetmealMobileController {
    @Reference
    SetmealService setmealService;
    @Autowired
    JedisPool jedisPool;
    //查找所有套餐
    @RequestMapping("/getSetmeal")
    public Result getSetmeal(){
        try {
            List<Setmeal> list = setmealService.getSetmeal();
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }
     //根据id查询套餐信息
    @RequestMapping("/getSetmealById")
    public Result getSetmealById(Integer id){
        try {
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }
}
