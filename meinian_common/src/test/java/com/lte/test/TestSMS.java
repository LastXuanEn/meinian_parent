package com.lte.test;

import com.lte.util.SMSUtils;

/**
 * @Auther: laite
 * @Date: 2021/8/12 - 08 - 12 - 22:07
 * @Description: com.lte.test
 * @version: 1.0
 */
public class TestSMS {
    //这是一个main方法，程序的入口
    public static void main(String[] args) {
        try {
            SMSUtils.sendShortMessage("18673314823","123456");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
