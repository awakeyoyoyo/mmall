package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
public class CookieUtil {
    private final static String COOKIE_DOMAIN="awakeyoyoyo.com";
    private final static String COOKIE_NAME="mmall_login_token";
    public static  String readLoginToken(HttpServletRequest request){
        Cookie[] cks=request.getCookies();
        if (cks!=null){
            for (Cookie ck:cks){
                log.info("read cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
                if (StringUtils.equals(ck.getName(),COOKIE_NAME)){
                    log.info("return cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
                    return ck.getValue();
                }
            }
        }
        return null;
    }
    public static  void writeLoginToken(HttpServletResponse response,String token){
        Cookie ck=new Cookie(COOKIE_NAME,token);
        ck.setDomain(COOKIE_DOMAIN);
        ck.setPath("/");//代表设置在根目录，若设置“test”则只有test下得页面和代码才可以获取cookie
        //单位是秒
        //如果不设置maxage，cookie就不会写入硬盘，而是写入内存，旨在当前页面有效。
        ck.setMaxAge(60*60*24*30);//如果是-1，则代表永恒
        log.info("write cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
        response.addCookie(ck);
    }


    public static void delLoginToken(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cks=request.getCookies();
        if (cks!=null){
            for (Cookie ck:cks){
                if (StringUtils.equals(ck.getName(),COOKIE_NAME)){
                    ck.setDomain(COOKIE_DOMAIN);
                    ck.setPath("/");
                    ck.setHttpOnly(true); //不允许脚本读取cookie
                    ck.setMaxAge(0);
                    log.info("del cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
                    response.addCookie(ck);
                    return;
                }
            }
        }

    }

}
