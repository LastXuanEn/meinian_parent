package com.lte.dao;

import com.github.pagehelper.Page;
import com.lte.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @Auther: laite
 * @Date: 2021/8/9 - 08 - 09 - 18:43
 * @Description: com.lte.dao
 * @version: 1.0
 */
public interface SetmealDao {
    void add(Setmeal setmeal);

    void setSetmealAndTravelGroup(Map<String, Integer> map);

    Page findPage(String queryString);


    List<Setmeal> getSetmeal();

    Setmeal findById(Integer id);


    List<Map<String, Object>> findSetmealCount();
}
