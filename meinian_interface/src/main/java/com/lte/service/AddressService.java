package com.lte.service;

import com.lte.entity.PageResult;
import com.lte.entity.QueryPageBean;
import com.lte.pojo.Address;

import java.util.List;

/**
 * @Auther: laite
 * @Date: 2021/8/17 - 08 - 17 - 15:08
 * @Description: com.lte.service
 * @version: 1.0
 */
public interface AddressService {
    List<Address> findAllMaps();

    PageResult findPage(QueryPageBean queryPageBean);

    void addAddress(Address address);

    void deleteById(Integer id);
}
