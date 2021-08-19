package com.lte.dao;

import com.lte.pojo.Order;

import java.util.List;
import java.util.Map;

/**
 * @Auther: laite
 * @Date: 2021/8/13 - 08 - 13 - 15:25
 * @Description: com.lte.dao
 * @version: 1.0
 */
public interface OrderDao {
    List<Order> findByCondition(Order order);

    void add(Order order1);

    Map<String, Object> findById(Integer id);

    Integer getTodayOrderNumber(String today);

    Integer getTodayVisitsNumber(String today);

    Integer getThisWeekAndMonthOrderNumber(Map<String, Object> paramWeek);

    Integer getThisWeekAndMonthVisitsNumber(Map<String, Object> paramWeekVisit);

    List<Map<String, Object>> findHotSetmeal();
}
