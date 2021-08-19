package com.lte.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lte.dao.TravelItemDao;
import com.lte.entity.PageResult;
import com.lte.entity.Result;
import com.lte.pojo.TravelItem;
import com.lte.service.TravelItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther: laite
 * @Date: 2021/8/6 - 08 - 06 - 18:25
 * @Description: com.lte.service.impl
 * @version: 1.0
 */
@Service(interfaceClass = TravelItemService.class )//发布服务，注册到zk中心
@Transactional //声明式事务，所有方法都增加事务
public class TravelItemServiceImpl implements TravelItemService {
    @Autowired
    TravelItemDao travelItemDao;

    @Override
    public void add(TravelItem travelItem) {
        travelItemDao.add(travelItem);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //1、初始化分页操作
        PageHelper.startPage(currentPage,pageSize); //limit ?,?  当前页:开始索引；每页记录数：每页查询的条数
        //2、使用sql语句进行查询
        Page page   =  travelItemDao.findPage(queryString); //返回当前页数据

        return new PageResult(page.getTotal(),page.getResult()); //1、总记录数 2、分页数据集合

    }

    @Override
    public void delete(Integer id) {
        travelItemDao.delete(id);
    }

    @Override
    public TravelItem getById(Integer id) {
        TravelItem travelItem = travelItemDao.getById(id);
        return travelItem;
    }

    @Override
    public void edit(TravelItem travelItem) {
        travelItemDao.edit(travelItem);
    }

    @Override
    public List<TravelItem> findAll() {
        List<TravelItem> list = travelItemDao.findAll();

        return list;
    }
}
