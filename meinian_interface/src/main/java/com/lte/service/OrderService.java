package com.lte.service;

import com.lte.entity.Result;

import java.util.Map;

/**
 * @Auther: laite
 * @Date: 2021/8/13 - 08 - 13 - 14:21
 * @Description: com.lte.service
 * @version: 1.0
 */
public interface OrderService {

    Result order(Map map) throws Exception;

    Map<String, Object> findById(Integer id);
}
