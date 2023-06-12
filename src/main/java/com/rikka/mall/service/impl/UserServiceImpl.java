package com.rikka.mall.service.impl;

import com.rikka.mall.dao.MallUserMapper;
import com.rikka.mall.enums.ResponseStatusEnum;
import com.rikka.mall.enums.UserRoleEnum;
import com.rikka.mall.pojo.MallUser;
import com.rikka.mall.pojo.MallUserExample;
import com.rikka.mall.service.IUserService;
import com.rikka.mall.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Yuno
 * @time 3:02 PM 5/30/2023
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {


    @Autowired
    private MallUserMapper mum;

    @Override
    public ResponseVO<MallUser> register(MallUser user) {
        // 1. unique field: username and password
        MallUserExample mallUserExample = new MallUserExample();
        mallUserExample.createCriteria().andUsernameEqualTo(user.getUsername());
        int cnt = mum.countByExample(mallUserExample);
        if (cnt > 0) {
            return ResponseVO.error(ResponseStatusEnum.USER_EXISTED);
        }

        mallUserExample.clear();
        mallUserExample.createCriteria().andEmailEqualTo(user.getEmail());
        cnt = mum.countByExample(mallUserExample);
        if (cnt > 0) {
            return ResponseVO.error(ResponseStatusEnum.EMAIL_BOUND);
        }

        // 2. use MD5 encode passwd
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));

        // 3. insert User info into database
        user.setRole(UserRoleEnum.NORMAL.getCode());
        int res = mum.insertSelective(user);
        if (res > 0) {
            log.info("注册成功， 用户信息为： {}", user);
            // don't return passwd
            user.setPassword("");
            return ResponseVO.success(user);
        } else {
            log.error("数据库异常, 注册时插入数据: {} 失败", user);
            return ResponseVO.error(ResponseStatusEnum.SERVER_ERROR);
        }
    }

    @Override
    public ResponseVO<MallUser> login(String username, String password) {
        MallUserExample mallUserExample = new MallUserExample();
        mallUserExample.createCriteria().andUsernameEqualTo(username);
        List<MallUser> mallUsers = mum.selectByExample(mallUserExample);
        if (mallUsers.isEmpty()) {
            return ResponseVO.error(ResponseStatusEnum.USERNAME_OR_PASSWD_ERROR);
        }

        MallUser user = mallUsers.get(0);
        // You must use MD5 to encode your password, and compare in ignoreCase mode.
        if(!user.getPassword().equalsIgnoreCase(DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)))) {
            return ResponseVO.error(ResponseStatusEnum.USERNAME_OR_PASSWD_ERROR);
        }
        // Don't return the password
        user.setPassword("");
        log.info("user: {} logged successfully", user);
        return ResponseVO.success(user);
    }
}
