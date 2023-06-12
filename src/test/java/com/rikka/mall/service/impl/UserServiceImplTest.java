package com.rikka.mall.service.impl;

import com.rikka.mall.MallApplicationTests;
import com.rikka.mall.enums.ResponseStatusEnum;
import com.rikka.mall.enums.UserRoleEnum;
import com.rikka.mall.pojo.MallUser;
import com.rikka.mall.service.IUserService;
import com.rikka.mall.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Yuno
 * @time 3:32 PM 5/30/2023
 */

@DisplayName("用户模块")
@Transactional
@Slf4j
class UserServiceImplTest extends MallApplicationTests {

    @Autowired
    private IUserService ius;

    MallUser user = new MallUser(null, "Yuno", "5201314rikka..", "abcdekl35@gmail.com", "18095045912", null, null, UserRoleEnum.ADMIN.getCode(),
            null, null);

    String userName = "Yukki";
    String passwd = "5201314rikka..";

    @Test
    @DisplayName("注册功能")
    void register() {
        ResponseVO<MallUser> register = ius.register(user);
        log.info("the register is: {}", gson.toJson(register));
        assertEquals(register.getStatus(), ResponseStatusEnum.SUCCESS.getCode());
    }

    @Test
    @DisplayName("登录功能")
    void login() {
        ResponseVO<MallUser> login = ius.login(userName, passwd);
        log.info("the login is: {}", gson.toJson(login));
        assertEquals(login.getStatus(), ResponseStatusEnum.SUCCESS.getCode());
    }

}