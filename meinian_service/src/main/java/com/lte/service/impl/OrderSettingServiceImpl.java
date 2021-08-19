package com.lte.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lte.dao.OrderSettingDao;
import com.lte.pojo.OrderSetting;
import com.lte.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: laite
 * @Date: 2021/8/11 - 08 - 11 - 13:48
 * @Description: com.lte.service.impl
 * @version: 1.0
 */
@Service(interfaceClass = OrderSettingService.class) //发布服务到zk注册中心
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    OrderSettingDao orderSettingDao;
    @Override
    public void add(List<OrderSetting> orderSettingList) {
        //1-第一步对集合数据进行迭代
        for (OrderSetting orderSetting : orderSettingList) {
            //2-判断一下数据库里面是否已经存在此日期的数据
            int count =orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
            if(count>0){
                //如果数据库已经存在此日期，那么直接进行覆盖
                orderSettingDao.edit(orderSetting);
            }else{
                //如果数据库不存在此日期，那么直接进行添加
                orderSettingDao.add(orderSetting);
            }
        }
    }

    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        //1-设置查询时间段
        String dateBegin = date+"-1";
        String dateEnd = date+"-31";
        Map<String, String> map = new HashMap();
        map.put("dateBegin",dateBegin);
        map.put("dateEnd",dateEnd);
        //2-将map传入DAO层做SQL查询 返回一个ListPojo集合
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        //3- new一个ListMap集合，将listPojo集合遍历，存放至新集合中
        List<Map> mapList = new ArrayList<>();
        for (OrderSetting setting : list) {
            HashMap<String, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("date",setting.getOrderDate().getDate());//获取几号日期
            objectObjectHashMap.put("number",setting.getNumber());//获取最大数量
            objectObjectHashMap.put("reservations",setting.getReservations());
            mapList.add(objectObjectHashMap);
        }
        return mapList;
    }

    @Override
    public void editNumberByOrderDate(Map map) {
        try {
            //将Map取出来分别调用dao的方法
            String str = (String) map.get("orderDate");
            String num = (String) map.get("value");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(str);
            //创建一个OrderSetting对象，重新设置值
            OrderSetting setting = new OrderSetting();
            setting.setOrderDate(date);
            setting.setNumber(Integer.parseInt(num));
            int count = orderSettingDao.findCountByOrderDate(date);
            if(count>0){
                //如果数据库里面有这个日期的数据，那么直接修改
                orderSettingDao.edit(setting);
            }else {
                //不存在，则直接添加
                orderSettingDao.add(setting);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
