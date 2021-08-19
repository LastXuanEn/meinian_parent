package com.lte.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lte.constant.RedisConstant;
import com.lte.dao.SetmealDao;
import com.lte.entity.PageResult;
import com.lte.entity.Result;
import com.lte.pojo.Setmeal;
import com.lte.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: laite
 * @Date: 2021/8/9 - 08 - 09 - 18:41
 * @Description: com.lte.service.impl
 * @version: 1.0
 */
@Service(interfaceClass = SetmealService.class) //发布服务到zk中心
@Transactional //发布事务
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    SetmealDao setmealDao;
    @Autowired
    JedisPool jedisPool;

    @Override
    public void add(Integer[] travelgroupIds, Setmeal setmeal) {
        //1 先添加主表 ,并且回显ID
        setmealDao.add(setmeal);
        //2 根据回显获取ID值，
        Integer id = setmeal.getId();
        //3、绑定套餐与抱团游的多对多关系
        setSetmealAndTravelGroup(id,travelgroupIds);
        //新添加，在添加窗口点击确定后，将img图片名上传到redis数据库
        String img = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,img);


    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //初始化页面 调用pageHelper
        PageHelper.startPage(currentPage, pageSize);
        //调用Dao持久化层，将查询结果封装到已经初始化好的page对象中
        Page page = setmealDao.findPage(queryString);

        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<Setmeal> getSetmeal() {
        List<Setmeal> setmeal = setmealDao.getSetmeal();
        return setmeal;
    }

    @Override
    public Setmeal findById(Integer id) {
        Setmeal setmeal = setmealDao.findById(id);
        return setmeal;
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        List<Map<String, Object>> listCount = setmealDao.findSetmealCount();
        return listCount;
    }


    private void setSetmealAndTravelGroup(Integer id,Integer[] travelgroupIds){
        for (Integer travelgroupId : travelgroupIds) {
            Map<String,Integer> map = new HashMap<>();
            map.put("setmealId",id);
            map.put("travelGroupId",travelgroupId);
            setmealDao.setSetmealAndTravelGroup(map);
        }
    }
}
