package com.lte.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lte.constant.MessageConstant;
import com.lte.entity.Result;
import com.lte.pojo.OrderSetting;
import com.lte.service.OrderSettingService;
import com.lte.util.POIUtils;
import com.sun.tools.doclint.Checker;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: laite
 * @Date: 2021/8/11 - 08 - 11 - 13:36
 * @Description: com.lte.controller
 * @version: 1.0
 *
 * //1.使用POI解析文件 得到List<String[]> list
 * //2.把List<String[]> list转成 List<OrderSetting> list
 * //3.调用业务 进行保存
 */
@RestController
@RequestMapping("/orderSetting")
public class OrderSettingController {
    @Reference
    OrderSettingService orderSettingService;

    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile){
        try {
            // 1-使用poi工具类解析excel文件，读取里面的内容
            List<String[]> list = POIUtils.readExcel(excelFile);
            //2-创建List<OrderSetting> 集合，用于封装 OrderSetting对象
            List<OrderSetting> orderSettingList = new ArrayList<>();
            for (String[] strings : list) {
                //3-获取到一行里面，每个表格数据，进行封装
                 OrderSetting orderSetting = new OrderSetting(new Date(strings[0]),
                         Integer.parseInt(strings[1]));
                orderSettingList.add(orderSetting);
            }
            //调用业务层进行保存,传入封装好OrderSetting对象的集合
            orderSettingService.add(orderSettingList);
            return new Result(true, MessageConstant.UPLOAD_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.UPLOAD_FAIL);
        }
    }
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){
        try {
            // 1.组织查询Map，dateBegin表示月份开始时间，dateEnd月份结束时间
            // 2.查询当前月份的预约设置
            // 3.将List<OrderSetting>，组织成List<Map>
            // 4- date 传的是2021-07类型的字符串
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }
    @RequestMapping("/editNumberByOrderDate")
    public Result editNumberByOrderDate(@RequestBody Map map){
        try {
            //根据所给日期去数据库修改number值 限定值
            orderSettingService.editNumberByOrderDate(map);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(true,MessageConstant.ORDERSETTING_FAIL);
        }

    }
}
