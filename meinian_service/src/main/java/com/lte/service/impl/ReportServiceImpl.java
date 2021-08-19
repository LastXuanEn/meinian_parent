package com.lte.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lte.dao.MemberDao;
import com.lte.dao.OrderDao;
import com.lte.service.ReportService;
import com.lte.util.DateUtils;
import org.jboss.netty.util.internal.DetectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: laite
 * @Date: 2021/8/16 - 08 - 16 - 16:19
 * @Description: com.lte.service.impl
 * @version: 1.0
 */

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {
    @Autowired
    MemberDao memberDao;

    @Autowired
    OrderDao orderDao;

    @Override
    public Map<String, Object> getBusinessReportData() {
        Map<String,Object> map= null;
        try {
            //日期工具类
            //1- 当前时间(格式化日期->字符串)
            String today = DateUtils.parseDate2String(DateUtils.getToday());
            // 2：本周（周一）
            String weekMonday  = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
            // 3：本周（周日）
            String weekSunday = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
            // 4：本月（1号）
            String monthFirst = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
            // 5：本月（31号）
            String monthLast = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());

            // （1）今日新增会员数
            Integer todayNewMember = memberDao.getTodayNewMember(today);
            // （2）总会员数
            Integer totalMember = memberDao.getTotalMember();
            // （3）本周新增会员数
            Integer thisWeekNewMember = memberDao.getThisWeekAndMonthNewMember(weekMonday);
            // （4）本月新增会员数
            Integer thisMonthNewMember = memberDao.getThisWeekAndMonthNewMember(monthFirst);

            // （5）今日预约数
            Integer todayOrderNumber = orderDao.getTodayOrderNumber(today);
            // （6）今日出游数
            Integer todayVisitsNumber = orderDao.getTodayVisitsNumber(today);
            // （7）本周预约数
            Map<String,Object> paramWeek = new HashMap<String,Object>();
            paramWeek.put("begin",weekMonday);
            paramWeek.put("end",weekSunday);
            Integer thisWeekOrderNumber = orderDao.getThisWeekAndMonthOrderNumber(paramWeek);

            // （9）本月预约数
            Map<String,Object> paramMonth = new HashMap<String,Object>();
            paramMonth.put("begin",monthFirst);
            paramMonth.put("end",monthLast);
            int thisMonthOrderNumber = orderDao.getThisWeekAndMonthOrderNumber(paramMonth);

            // （8）本周出游数
            Map<String,Object> paramWeekVisit = new HashMap<String,Object>();
            paramWeekVisit.put("begin",weekMonday);
            paramWeekVisit.put("end",weekSunday);
            Integer thisWeekVisitsNumber = orderDao.getThisWeekAndMonthVisitsNumber(paramWeekVisit);

            // （10）本月出游数
            Map<String,Object> paramMonthVisit = new HashMap<String,Object>();
            paramMonthVisit.put("begin",monthFirst);
            paramMonthVisit.put("end",monthLast);
            Integer thisMonthVisitsNumber = orderDao.getThisWeekAndMonthVisitsNumber(paramMonthVisit);

            // （11）热门套餐
            List<Map<String,Object>> hotSetmeal = orderDao.findHotSetmeal();

            map = new HashMap<String,Object>();
            map.put("reportDate",today);
            map.put("todayNewMember",todayNewMember);
            map.put("totalMember",totalMember);
            map.put("thisWeekNewMember",thisWeekNewMember);
            map.put("thisMonthNewMember",thisMonthNewMember);

            map.put("todayOrderNumber",todayOrderNumber);
            map.put("todayVisitsNumber",todayVisitsNumber);
            map.put("thisWeekOrderNumber",thisWeekOrderNumber);
            map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
            map.put("thisMonthOrderNumber",thisMonthOrderNumber);
            map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);

            map.put("hotSetmeal",hotSetmeal);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return map;
    }
}
