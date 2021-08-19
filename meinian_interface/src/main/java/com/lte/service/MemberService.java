package com.lte.service;

import com.lte.pojo.Member;

import java.util.List;
import java.util.Map;

/**
 * @Auther: laite
 * @Date: 2021/8/14 - 08 - 14 - 11:04
 * @Description: com.lte.service
 * @version: 1.0
 */
public interface MemberService {
    Member findByTelephone(String telephone);

    void add(Member member);

    List<Integer> findMemberCountByMonth(List<String> list);


}
