package com.lte.dao;

import com.github.pagehelper.Page;
import com.lte.pojo.TravelGroup;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Auther: laite
 * @Date: 2021/8/8 - 08 - 08 - 1:11
 * @Description: com.lte.dao
 * @version: 1.0
 */
public interface TravelGroupDao {
    void setTravelGroupAndTravelItem(Map<String, Integer> map);

    void add(TravelGroup travelGroup);


    Page findPage(String queryString);

    void delete(Integer id);

    void deleteMidTable(Integer id);

    TravelGroup findById(Integer id);

    List<Integer> findTravelItemIdByTravelGroupId(Integer id);

    void edit(TravelGroup travelGroup);

    List<TravelGroup> findAll();

    long findCountMidTableById(Integer id);

    List<TravelGroup> findTravelGroupListById(Integer id);
}
