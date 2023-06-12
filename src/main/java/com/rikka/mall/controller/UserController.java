package com.rikka.mall.controller;

import com.rikka.mall.consts.MallConst;
import com.rikka.mall.enums.ResponseStatusEnum;
import com.rikka.mall.form.MallUserLoginForm;
import com.rikka.mall.form.MallUserRegisterForm;
import com.rikka.mall.pojo.MallUser;
import com.rikka.mall.service.ICategoryService;
import com.rikka.mall.service.IUserService;
import com.rikka.mall.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author Yuno
 * @time 4:23 PM 5/30/2023
 */

@Slf4j
@RestController
public class UserController {

    @Autowired
    private IUserService ius;

    @Autowired
    private ICategoryService ics;

    /**
     * @param userForm 提交的表单信息
     * @return 返回给前端的信息
     */
    @PostMapping("/user/register")
    public ResponseVO<MallUser> register(@Valid @RequestBody MallUserRegisterForm userForm) {
        // TODO 验证表单-后端校验
        log.info("The Register userForm is: {}", userForm);
        MallUser user = new MallUser();
        BeanUtils.copyProperties(userForm, user);
        return ius.register(user);
    }


    /**
     * @param userForm 登錄表單
     * @param session  會話對象
     * @return 響應信息
     */
    @PostMapping("/user/login")
    public ResponseVO<MallUser> login(@Valid @RequestBody MallUserLoginForm userForm,
                                      HttpSession session) {

        // put the user to session
        ResponseVO<MallUser> user = ius.login(userForm.getUsername(), userForm.getPassword());
        session.setAttribute(MallConst.SESSION_USER_KEY, user.getData());
        return user;
    }


    /**
     * @param session 會話對象
     * @return 響應信息
     */
    @GetMapping("/user")
    public ResponseVO<MallUser> checkLogin(HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.SESSION_USER_KEY);
        return ResponseVO.success(user);
    }


    /**
     * 退出登录一般使用POST请求
     * @param session 會話對象
     * @return 響應信息
     */
    @PostMapping("/user/logout")
    public ResponseVO<MallUser> logout(HttpSession session) {
        session.removeAttribute(MallConst.SESSION_USER_KEY);
        return ResponseVO.success();
    }

}

