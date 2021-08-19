package com.lte.dao;

import com.lte.pojo.Member;

/**
 * @Auther: laite
 * @Date: 2021/8/13 - 08 - 13 - 15:23
 * @Description: com.lte.dao
 * @version: 1.0
 */
public interface MemberDao {
    Member findByTelephone(String telephone);

    void add(Member member);

    Integer findMemberCountByMonth(String s);

    Integer getTodayNewMember(String today);

    Integer getTotalMember();

    Integer getThisWeekAndMonthNewMember(String date);
}
