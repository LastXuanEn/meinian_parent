package com.lte.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lte.dao.AddressDao;
import com.lte.entity.PageResult;
import com.lte.entity.QueryPageBean;
import com.lte.pojo.Address;
import com.lte.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther: laite
 * @Date: 2021/8/17 - 08 - 17 - 15:08
 * @Description: com.lte.service.impl
 * @version: 1.0
 */
@Service(interfaceClass = AddressService.class)
@Transactional
public class AddressServiceImpl implements AddressService {
    @Autowired
    AddressDao addressDao;

    @Override
    public List<Address> findAllMaps() {
        List<Address> list =  addressDao.findAllMaps();
        return list;
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //初始化分页插件
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        //封装page
        Page page = addressDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void addAddress(Address address) {
        addressDao.addAddress(address);
    }

    @Override
    public void deleteById(Integer id) {
        addressDao.deleteById(id);
    }
}
