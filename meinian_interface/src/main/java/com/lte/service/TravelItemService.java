package com.lte.service;

import com.lte.entity.PageResult;
import com.lte.pojo.TravelItem;

import java.util.List;

/**
 * @Auther: laite
 * @Date: 2021/8/6 - 08 - 06 - 18:23
 * @Description: com.lte.service
 * @version: 1.0
 */
public interface TravelItemService {

    void add (TravelItem travelItem);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    void delete(Integer id);

    TravelItem getById(Integer id);

    void edit(TravelItem travelItem);

    List<TravelItem> findAll();

}
