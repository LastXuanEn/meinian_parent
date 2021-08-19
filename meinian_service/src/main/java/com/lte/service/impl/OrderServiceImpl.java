package com.lte.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lte.constant.MessageConstant;
import com.lte.dao.MemberDao;
import com.lte.dao.OrderDao;
import com.lte.dao.OrderSettingDao;
import com.lte.entity.Result;
import com.lte.pojo.Member;
import com.lte.pojo.Order;
import com.lte.pojo.OrderSetting;
import com.lte.service.OrderService;
import com.lte.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: laite
 * @Date: 2021/8/13 - 08 - 13 - 14:22
 * @Description: com.lte.service.impl
 * @version: 1.0
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;
    //旅游预约
    @Override
    public Result order(Map map) throws Exception{
        //检查当前日期是否进行了预约设置
        String orderDate = (String)map.get("orderDate");
        //因为数据库里面是DAte类型，http协议传输的是字符串类型，所以需要转换
        Date date = DateUtils.parseString2Date(orderDate);
        //使用预约时间查询预约设置表，看看是否可以进行预约
        //（1）使用预约时间，查询预约设置表，判断是否有该记录
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        // 如果预约设置表等于null，说明不能进行预约，压根就没有开团
        if(orderSetting == null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }else{
            //如果有，说明预约可以进行预约,此时在预约设置表里面对比一下已预约人数和可约人数
            int number = orderSetting.getNumber();
            int reservations = orderSetting.getReservations();
            if(reservations>=number){
                //结束方法,预约已满
                return new Result(false,MessageConstant.ORDER_FULL);
            }
            //获取手机号码
            String telephone = (String) map.get("telephone");
            //（2）使用手机号进行查询，判断此人是否为会员
            Member member =  memberDao.findByTelephone(telephone);
            //如果是会员，防止重复预约（一个会员、一个时间、一个套餐不能重复，否则是重复预约
            if (member!= null){
                Integer memberId = member.getId();
                //获取套餐ID
                Integer setmealId = Integer.parseInt((String)map.get("setmealId"));
                Order order = new Order(memberId, date, null, null, setmealId);
                // 根据预约信息查询是否已经预约
                List<Order> list = orderDao.findByCondition(order);
                //如果List等于null，则代表可以预约，不等于null，则不能重复预约
                if (list != null && list.size() > 0){
                    //已经完成了预约，不能重复预约
                    return new Result(false, MessageConstant.HAS_ORDERED);
                }
                //（3）可以进行预约，更新预约设置表中预约人数的值，使其的值+1
                //可以预约，设置预约人数加一
                orderSetting.setReservations(orderSetting.getReservations() + 1);
                orderSettingDao.editReservationsByOrderDate(orderSetting);

                //（4）可以进行预约，向预约表中添加1条数据
                //保存预约信息到预约表
                Order order1 = new Order();
                order1.setMemberId(member.getId()); //会员id
                order1.setOrderDate(date); // 预约时间
                order1.setOrderStatus(Order.ORDERSTATUS_NO); // 预约状态（已出游/未出游）
                order1.setOrderType((String)map.get("orderType"));
                order1.setSetmealId(Integer.parseInt((String)map.get("setmealId")));
                orderDao.add(order1);

                return new Result(true, MessageConstant.ORDER_SUCCESS, order);



            }else{
                //如果不是会员：注册会员
                member = new Member();
                member.setName((String)map.get("name"));
                member.setSex((String)map.get("sex"));
                member.setPhoneNumber((String)map.get("telephone"));
                member.setIdCard((String)map.get("idCard"));
                member.setRegTime(new Date()); // 会员注册时间，当前时间
                memberDao.add(member);

                orderSetting.setReservations(orderSetting.getReservations() + 1);
                orderSettingDao.editReservationsByOrderDate(orderSetting);

                //（4）可以进行预约，向预约表中添加1条数据
                //保存预约信息到预约表
                Order order2 = new Order();
                order2.setMemberId(member.getId()); //会员id
                order2.setOrderDate(date); // 预约时间
                order2.setOrderStatus(Order.ORDERSTATUS_NO); // 预约状态（已出游/未出游）
                order2.setOrderType((String)map.get("orderType"));
                order2.setSetmealId(Integer.parseInt((String)map.get("setmealId")));
                order2.setOrderType("微信预约");
                orderDao.add(order2);

                return new Result(true, MessageConstant.ORDER_SUCCESS, order2);


            }
        }

    }

    @Override
    public Map<String, Object> findById(Integer id) {
        try {
            Map<String, Object>  map = orderDao.findById(id);
            Date date = (Date)map.get("orderDate");
            String stringDate = DateUtils.parseDate2String(date);
            map.put("orderDate",stringDate);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
