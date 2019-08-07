package com.mmall.controller.common;


import com.mmall.common.Const;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SessionExpireFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest=(HttpServletRequest)servletRequest;
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(loginToken)){
            //判断loginToken是否为空或者""
            // 如果不为空得话，符合条件，继续Getuser信息
            String userJsonStr= RedisShardedPoolUtil.get(loginToken);
            User user= JsonUtil.stringToObj(userJsonStr, User.class);
            if (user!=null){
                //如果user不为空，则重置session时间，即调用expire
                RedisShardedPoolUtil.expire(loginToken, Const.RedisCacheExpireTime.REDIS_SESSION_EXTIME);
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
