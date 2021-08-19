package com.lte.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lte.dao.TravelGroupDao;
import com.lte.entity.PageResult;
import com.lte.entity.Result;
import com.lte.pojo.TravelGroup;
import com.lte.service.TravelGroupService;
import com.lte.service.TravelItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: laite
 * @Date: 2021/8/8 - 08 - 08 - 1:09
 * @Description: com.lte.service.impl
 * @version: 1.0
 */

@Service(interfaceClass = TravelGroupService.class )//发布服务，注册到zk中心
@Transactional //声明式事务，所有方法都增加事务
public class TravelGroupServiceImpl implements TravelGroupService {
    @Autowired
    TravelGroupDao travelGroupDao;

    @Override
    //新增跟团游
    public void add(TravelGroup travelGroup, Integer[] travelItemIds) {
        // 1 新增跟团游，想t_travelgroup中添加数据，新增后返回新增的id
        travelGroupDao.add(travelGroup);
        // 2 新增跟团游和自由行中间表t_travelgroup_travelitem新增数据(新增几条，由travelItemIds决定)
        Integer travelGroupId = travelGroup.getId();
        setTravelGroupAndTravelItem(travelGroupId,travelItemIds);

    }

    @Override
    //分页查询
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //初始化页面,使用分页插件PageHelper，设置当前页，每页最多显示的记录数
        PageHelper.startPage(currentPage,pageSize);
        //响应分页插件的Page对象,将数据封装到初始化好的Page中
        Page page = travelGroupDao.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    //删除跟团游
    public void delete(Integer id) {
        /*主表的被引用列要求是一个key（一般就是主键）
        * 插入数据，先插入主表
           删除数据，先删除从表
        *   中间表：先删除后添加
        * 先去中间表去做一次查询
        * 如果数量大于0就做两次级联删除
        * 如果小于0就直接删除抱团游
        * */
       Long count =  travelGroupDao.findCountMidTableById(id);
       if(count>0){
           travelGroupDao.deleteMidTable(id);
           travelGroupDao.delete(id);
       }else{
           travelGroupDao.delete(id);
       }
    }

    @Override
    //通过ID查找抱团游
    public TravelGroup findById(Integer id) {
        TravelGroup travelGroup = travelGroupDao.findById(id);
        return travelGroup;
    }

    @Override
    //根据抱团游ID去中间表查询自由行的IDS
    public List<Integer> findTravelItemIdByTravelGroupId(Integer id) {
        List<Integer> list =  travelGroupDao.findTravelItemIdByTravelGroupId(id);
        return list;
    }

    @Override
    public void edit(TravelGroup travelGroup, Integer[] travelItemIds) {
        //1、编辑跟团游
        travelGroupDao.edit(travelGroup);
        Integer travelGroupId = travelGroup.getId();
        //2、删除中间表数据
        travelGroupDao.deleteMidTable(travelGroupId);
        //3、再添加关联数据
        setTravelGroupAndTravelItem(travelGroupId,travelItemIds);
    }

    @Override
    public List<TravelGroup> findAll() {
        List<TravelGroup> list =  travelGroupDao.findAll();
        return list;
    }


    private void setTravelGroupAndTravelItem(Integer travelGroupId,Integer[] travelItemIds){
        /**
         * @Description 定义service的私有方法 目的新增跟团游和自由行中间表新增数据
         * @Param [travelGroupId, travelItemIds]
         * @return void
         **/
        // 新增几条数据，由travelItemIds的长度决定
        for (Integer travelItemId : travelItemIds) {
            Map<String, Integer> map = new HashMap<>();
            map.put("travelGroup",travelGroupId);
            map.put("travelItem",travelItemId);
            travelGroupDao.setTravelGroupAndTravelItem(map);
        }
    }

}
