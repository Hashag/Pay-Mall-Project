package com.rikka.mall.interceptor;

import com.rikka.mall.consts.MallConst;
import com.rikka.mall.exception.NeedLoginException;
import com.rikka.mall.pojo.MallUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Yuno
 * @time 10:26 AM 5/31/2023
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        MallUser user = (MallUser) session.getAttribute(MallConst.SESSION_USER_KEY);
        if (user == null) {
            log.info("用戶未登錄, 請求已被攔截, the url is: {}", request.getRequestURL());
            throw new NeedLoginException("未登錄");
        }
        return true;
    }
}
