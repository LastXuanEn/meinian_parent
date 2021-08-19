package com.lte.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lte.dao.MemberDao;
import com.lte.pojo.Member;
import com.lte.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: laite
 * @Date: 2021/8/14 - 08 - 14 - 11:04
 * @Description: com.lte.service.impl
 * @version: 1.0
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberDao memberDao;
    @Override
    public Member findByTelephone(String telephone) {
        Member member = memberDao.findByTelephone(telephone);
        return member;
    }

    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByMonth(List<String> list) {
        List<Integer> listCount = new ArrayList<>();
        for (String s : list) {
           Integer count = memberDao.findMemberCountByMonth(s);
           listCount.add(count);
        }
        return listCount;
    }

}
