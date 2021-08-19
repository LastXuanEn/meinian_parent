package com.lte.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lte.constant.MessageConstant;
import com.lte.constant.RedisConstant;
import com.lte.entity.PageResult;
import com.lte.entity.QueryPageBean;
import com.lte.entity.Result;
import com.lte.pojo.Setmeal;
import com.lte.service.SetmealService;
import com.lte.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

/**
 * @Auther: laite
 * @Date: 2021/8/9 - 08 - 09 - 18:29
 * @Description: com.lte.controller
 * @version: 1.0
 */
@RestController //组合注解 ResponseBody+ Controller
@RequestMapping("/setmeal")
public class SetmealController {
    //RPC远程调用Service
    @Reference
    SetmealService setmealService;
    @Autowired
    JedisPool jedisPool;
    @RequestMapping("/add")
    public Result add (Integer[] travelgroupIds, @RequestBody Setmeal setmeal){
        try {
            setmealService.add(travelgroupIds, setmeal);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }
    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile){
        try {
            // imgFile:需要跟页面el-upload里面的name保持一致
            //1、获取文件原始名字
            String filename = imgFile.getOriginalFilename();
            //找到文件最后出现的位置
            int index = filename.lastIndexOf(".");
            //获取文件后缀
            String suffix = filename.substring(index);
            //2、使用UUID随机产生文件名，重新命名
            String name = UUID.randomUUID().toString() + suffix;
            String replace = name.replace("-", "");
            //3、调用工具类，上传图片至七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),replace);
            //4、补充代码 将上传图片名称存入 Redis,基于Redis的Set集合存储
            //解决问题，七牛云的垃圾图片
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,replace);

            return new Result(true,MessageConstant.UPLOAD_SUCCESS,replace);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }

    }
    //分页查询
    @RequestMapping("/findPage")
    //POST请求 用@RequestBody接收 封装成一个Bean对象
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        //调用service业务逻辑层
        PageResult pageResult = setmealService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }
}
