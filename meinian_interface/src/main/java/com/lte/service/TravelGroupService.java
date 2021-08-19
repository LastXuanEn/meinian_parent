package com.lte.service;

import com.lte.entity.PageResult;
import com.lte.pojo.TravelGroup;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Auther: laite
 * @Date: 2021/8/8 - 08 - 08 - 1:09
 * @Description: com.lte.service
 * @version: 1.0
 */
public interface TravelGroupService {

    //添加抱团游
    void add(TravelGroup travelGroup, Integer[] travelItemIds);
    //分页查询
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);
    //删除抱团游
    void delete(Integer id);
    //通过ID查找抱团游
    TravelGroup findById(Integer id);

    List<Integer> findTravelItemIdByTravelGroupId(Integer id);

    void edit(TravelGroup travelGroup,Integer[] travelItemIds);


    List<TravelGroup> findAll();
}
