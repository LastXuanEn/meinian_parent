package com.lte.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lte.constant.MessageConstant;
import com.lte.entity.PageResult;
import com.lte.entity.QueryPageBean;
import com.lte.entity.Result;
import com.lte.pojo.Address;
import com.lte.service.AddressService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: laite
 * @Date: 2021/8/17 - 08 - 17 - 15:06
 * @Description: com.lte.controller
 * @version: 1.0
 */
@RestController
@RequestMapping("/address")
public class AddressController {
    @Reference
    AddressService addressService;

    @RequestMapping("/findAllMaps")
    public Map<String, List> findAllMaps(){
        //1- 这此方法的需要返回的结果：Map集合
        Map<String,List> resultMap = new HashMap<>();
        //2- 这是封装坐标的list集合
        List<Map> listGridn = new ArrayList<>();
        //3- 这是封装地址名字的List集合
        List<Map> listAddressName = new ArrayList<>();


        List<Address> list = addressService.findAllMaps();
        for (Address address : list) {
            //4- 遍历地址名字，存入map,再存入list
            String addressName = address.getAddressName();
            Map<String, String> mapAddressName = new HashMap<>();
            mapAddressName.put("addressName",addressName);
            listAddressName.add(mapAddressName);

            //5- 遍历坐标的名字，存入map，再存入list
            String lng = address.getLng();
            String lat = address.getLat();
            Map<String,String> mapGridn = new HashMap<>();
            mapGridn.put("lng", lng);
            mapGridn.put("lat",lat);
            listGridn.add(mapGridn);
        }
        //6- 将封装map的list集合装入map中
        resultMap.put("gridnMaps",listGridn);
        resultMap.put("nameMaps",listAddressName);
        return resultMap;
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
       PageResult pageResult =  addressService.findPage(queryPageBean);
       return pageResult;
    }

    @RequestMapping("/addAddress")
    public Result addAddress(@RequestBody Address address ){
        try {
            addressService.addAddress(address);
            return new Result(true, MessageConstant.ADD_ADDRESS_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_ADDRESS_FAIL);
        }

    }
    @RequestMapping("/deleteById")
    public Result deleteById(Integer id){
        try {
            addressService.deleteById(id);
            return new Result(true,MessageConstant.DELETE_ADDRESS_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_ADDRESS_FAIL);
        }
    }
}
