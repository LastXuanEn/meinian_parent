package com.lte.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lte.constant.MessageConstant;
import com.lte.entity.PageResult;
import com.lte.entity.QueryPageBean;
import com.lte.entity.Result;
import com.lte.pojo.TravelItem;
import com.lte.service.TravelItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;
import java.util.List;

/**
 * @Auther: laite
 * @Date: 2021/8/6 - 08 - 06 - 18:21
 * @Description: com.lte.controller
 * @version: 1.0
 */
@RequestMapping("/travelItem")
@RestController //组合注解，controller+ResponseBody
public class TravelItemController {

    @Reference //远程调用服务
        TravelItemService travelItemService;
    @RequestMapping("/findAll")
    public Result findAll(){
        List<TravelItem> list = travelItemService.findAll();
        return new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS,list);
    }




    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('TRAVELITEM_EDIT')")
    public Result edit(@RequestBody TravelItem travelItem){
        try {
            travelItemService.edit(travelItem);
            return new Result(true,MessageConstant.EDIT_TRAVELITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_TRAVELITEM_FAIL);
        }

    }

    @RequestMapping("/add")
    public Result add(@RequestBody TravelItem travelItem){  //利用@requestbody作为入参，接收json对象
        try {
            travelItemService.add(travelItem);
            return new Result(true,MessageConstant.ADD_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_TRAVELITEM_FAIL);
        }
    }
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = travelItemService.findPage(queryPageBean.getCurrentPage(),
        queryPageBean.getPageSize(),queryPageBean.getQueryString());
        return pageResult;
    }

    @RequestMapping("delete")   //增删改返回的结果都是Result
    @PreAuthorize("hasAuthority('TRAVELITEM_DELETE')")                           //如果请求参数的参数名与处理方法的形参名一致，可以不加该注解
    public Result delete(Integer id){
        try {
            travelItemService.delete(id);
            return new Result(true,MessageConstant.DELETE_TRAVELITEM_SUCCESS);
        } catch (Exception e) {
            return new Result(false,MessageConstant.DELETE_TRAVELITEM_FAIL);
        }
    }
    @RequestMapping("getById")
    public Result getById(Integer id){
        try {
            //调用service层getById方法，并返回一条封装好的pojo对象
            TravelItem travelItem =  travelItemService.getById(id);
            return new Result(true,MessageConstant.QUERY_TRAVELITEM_SUCCESS,travelItem);
        } catch (Exception e) {
            TravelItem travelItem = new TravelItem();
            return new Result(false,MessageConstant.QUERY_TRAVELITEM_FAIL,travelItem);
        }
    }



}