package com.lte.test;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Auther: laite
 * @Date: 2021/8/15 - 08 - 15 - 17:29
 * @Description: com.lte.test
 * @version: 1.0
 */
public class TestSpringSecurity {
    @Test
    public void doSome(){
        /*  s = $2a$10$rnNF3nyyHWiSp.h.4oBUa.lA09l.bSHrHuCgBkYmvz87RRyVi3Jbm
            s1 = $2a$10$J0e1Jp4pBOd9By3xOwN1jOzORciItukniV0ZFwf/XfHt0vi.g35wq
        */
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String s = encoder.encode("123");
        System.out.println("s = " + s);
       String s1 =  encoder.encode("123");
        System.out.println("s1 = " + s1);

        boolean a = encoder.matches("123", "$2a$10$rnNF3nyyHWiSp.h.4oBUa.lA09l.bSHrHuCgBkYmvz87RRyVi3Jbm");
        System.out.println("a = " + a);

        boolean b = encoder.matches("123","$2a$10$J0e1Jp4pBOd9By3xOwN1jOzORciItukniV0ZFwf/XfHt0vi.g35wq");
        System.out.println("b = " + b);


    }
}
