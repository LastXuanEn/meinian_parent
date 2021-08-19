package com.lte.service;

import com.lte.entity.PageResult;
import com.lte.entity.Result;
import com.lte.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @Auther: laite
 * @Date: 2021/8/9 - 08 - 09 - 18:40
 * @Description: com.lte.service
 * @version: 1.0
 */
public interface SetmealService {
    void add(Integer[] travelgroupIds, Setmeal setmeal);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);


    List<Setmeal> getSetmeal();

    Setmeal findById(Integer id);


    List<Map<String, Object>> findSetmealCount();
}
