package com.lte.service;

import com.lte.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @Auther: laite
 * @Date: 2021/8/11 - 08 - 11 - 13:48
 * @Description: com.lte.service
 * @version: 1.0
 */
public interface OrderSettingService {
    void add(List<OrderSetting> orderSettingList);

    List<Map>getOrderSettingByMonth(String date);

    void editNumberByOrderDate(Map map);
}
