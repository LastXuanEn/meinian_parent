package com.lte.dao;

import com.github.pagehelper.Page;
import com.lte.pojo.TravelItem;

import java.util.List;

/**
 * @Auther: laite
 * @Date: 2021/8/6 - 08 - 06 - 18:32
 * @Description: com.lte.dao
 * @version: 1.0
 */
public interface TravelItemDao {
    //新增自由行
    void add(TravelItem travelItem);
    //分页查询
    Page findPage (String queryString);
    //删除单挑数据
    void delete(Integer id);
    //获取单条数据
    TravelItem getById(Integer id);
    //编辑单条数据
    void edit(TravelItem travelItem);

    //查询自由行的所有数据结果集
    List<TravelItem> findAll();

    //根据团游ID查询自由行集合
    List<TravelItem> findTravelItemListById(Integer id);
}
