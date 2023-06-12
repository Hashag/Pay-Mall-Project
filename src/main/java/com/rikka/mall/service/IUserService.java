package com.rikka.mall.service;

import com.rikka.mall.pojo.MallUser;
import com.rikka.mall.vo.ResponseVO;

/**
 * @author Yuno
 * @time 3:01 PM 5/30/2023
 */
public interface IUserService {

    /**
     *
     * @param user 注册信息
     * @return 返回给页面的响应
     */
    ResponseVO<MallUser> register(MallUser user);

    /**
     *
     * @param username 查詢的用戶名
     * @param password  查詢的密碼
     * @return 返回給前端的響應
     */
    ResponseVO<MallUser> login(String username, String password);
}
