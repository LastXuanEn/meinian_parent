package com.lte.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lte.constant.MessageConstant;
import com.lte.entity.PageResult;
import com.lte.entity.QueryPageBean;
import com.lte.entity.Result;
import com.lte.pojo.TravelGroup;
import com.lte.service.TravelGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: laite
 * @Date: 2021/8/8 - 08 - 08 - 1:06
 * @Description: com.lte.controller
 * @version: 1.0
 */
@RequestMapping("/travelGroup")
@RestController  //组合注解，controller+ResponseBody
public class TravelGroupController {
    @Reference   //RPC远程调用Service
    TravelGroupService travelGroupService;

    @RequestMapping("/add")
    public Result add(@RequestBody TravelGroup travelGroup,Integer[] travelItemIds){
        travelGroupService.add(travelGroup,travelItemIds);
        return new Result(true, MessageConstant.ADD_TRAVELGROUP_SUCCESS);
    }

    @RequestMapping("findPage")
    public PageResult findPage (@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = travelGroupService.findPage(queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),queryPageBean.getQueryString());

        return pageResult;
    }
    @RequestMapping("/delete")
    public Result delete (Integer id){
        try {
            travelGroupService.delete(id);
            return new Result(true,MessageConstant.DELETE_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            return new Result(false,MessageConstant.DELETE_TRAVELGROUP_FAIL);
        }
    }
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            TravelGroup travelGroup = travelGroupService.findById(id);
            return new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS,
                    travelGroup);
        } catch (Exception e) {
            return new Result(true,MessageConstant.QUERY_TRAVELGROUP_FAIL);
        }
    }
    @RequestMapping("/findTravelItemIdByTravelGroupId")
    public List<Integer> findTravelItemIdByTravelGroupId(Integer id){
        List<Integer> list = travelGroupService.findTravelItemIdByTravelGroupId(id);
        return list;
    }
    @RequestMapping("/edit")
    public Result edit(@RequestBody TravelGroup travelGroup, Integer[] travelItemIds){
        try {
            travelGroupService.edit(travelGroup,travelItemIds);
            return new Result(true, MessageConstant.EDIT_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_TRAVELITEM_FAIL);
        }
    }
    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<TravelGroup> list =  travelGroupService.findAll();
            return new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.QUERY_TRAVELGROUP_FAIL);
        }
    }
}
