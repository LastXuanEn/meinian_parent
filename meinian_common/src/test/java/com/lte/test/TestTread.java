package com.lte.test;

/**
 * @Auther: laite
 * @Date: 2021/8/13 - 08 - 13 - 16:43
 * @Description: com.lte.test
 * @version: 1.0
 */
public class TestTread {
    //这是一个main方法，程序的入口
    public static void main(String[] args) {
        int[] ints = new int[]{10,9,8,7,6,5,4,3,2,1};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (ints){
                    for (int i = 0; i < ints.length; i++) {
                        System.out.println("i = " + i);
                    }
                }
            }
        });
    }
}
