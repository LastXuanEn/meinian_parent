package com.lte.job;

import com.lte.constant.RedisConstant;
import com.lte.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Iterator;
import java.util.Set;

/**
 * @Auther: laite
 * @Date: 2021/8/10 - 08 - 10 - 16:21
 * @Description: com.lte.job
 * @version: 1.0
 */
public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;
    //清理图片
    public void clearImg(){
        //计算redis中两个集合的差值，获取垃圾图片名称
        // 需要注意：在比较的时候，数据多的放到前面，如果pic多，那么pic放到前面，db多，db放到前面
        /*总结Quartz定时清理七牛云上垃圾图片方法：
        此方法利用Quartz组件实现了定时清除了
        1、七牛云上的垃圾的图片（上传未提交的）
        2、同步清除redis两张表中的交集数据
        此方法的实现规则就是，在controller文件上传的方法中成功后，
        自动将此文件名设置在pic表中，
        当用户触发确定提交事件后，将此文件名再次传入到redis db表中，同步的也将文件名
        传入Mysql数据库中
        */
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        Iterator<String> iterator = set.iterator();
        while(iterator.hasNext()){
            String pic = iterator.next();
            System.out.println("删除图片的名称是："+pic);
            //删除图片服务器中的图片文件
            QiniuUtils.deleteFileFromQiniu(pic);
            //删除redis中的数据
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,pic);
        }
    }
}


