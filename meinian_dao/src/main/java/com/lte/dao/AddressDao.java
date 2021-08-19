package com.lte.dao;

import com.github.pagehelper.Page;
import com.lte.pojo.Address;

import java.util.List;

/**
 * @Auther: laite
 * @Date: 2021/8/17 - 08 - 17 - 15:11
 * @Description: com.lte.dao
 * @version: 1.0
 */
public interface AddressDao {
    List<Address> findAllMaps();

    Page findPage(String queryString);

    void addAddress(Address address);

    void deleteById(Integer id);
}
