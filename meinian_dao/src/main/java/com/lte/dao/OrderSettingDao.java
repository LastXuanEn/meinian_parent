package com.lte.dao;

import com.lte.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: laite
 * @Date: 2021/8/11 - 08 - 11 - 13:52
 * @Description: com.lte.dao
 * @version: 1.0
 */
public interface OrderSettingDao {
    int findCountByOrderDate(Date orderDate);

    void edit(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);


    List<OrderSetting> getOrderSettingByMonth(Map<String, String> map);

    OrderSetting findByOrderDate(Date date);

    void editReservationsByOrderDate(OrderSetting orderSetting);
}
