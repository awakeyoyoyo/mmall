package com.mmall.test;

import org.junit.Test;

import java.math.BigDecimal;

public class BigDecimalTest {
    @Test
    public void test1(){
        System.out.println(0.05+0.01);
        System.out.println(1.0-0.42);
        System.out.println(4.015*100);
        System.out.println(123.3/100);
    }
    @Test
    public void test2(){
        BigDecimal b1=new BigDecimal(0.05);
        BigDecimal b2=new BigDecimal(0.01);
        System.out.println(b1.add(b2));
    }
    @Test
    public void test3(){
        BigDecimal b1=new BigDecimal("0.05");
        BigDecimal b2=new BigDecimal("0.01");
        System.out.println(b1.add(b2));
    }
    @Test
    public void test4(){
        String s1 = new String("key");
        String s2 = new String("key");
        String s3 ="ABC";
        String s4 ="ABC";
        System.out.println("s1.equals(s2):"+s1.equals(s2));
        System.out.println("s1.equals(s3):"+s1.equals(s3));
        System.out.println("s3.equals(s4):"+s3.equals(s4));
        System.out.println(s1==s2);
        System.out.println(s1==s3);
        System.out.println(s3==s4);
    }
}
