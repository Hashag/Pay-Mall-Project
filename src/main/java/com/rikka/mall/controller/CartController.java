package com.rikka.mall.controller;

import com.rikka.mall.consts.MallConst;
import com.rikka.mall.form.CartAddForm;
import com.rikka.mall.form.CartUpdateForm;
import com.rikka.mall.pojo.MallUser;
import com.rikka.mall.service.ICartService;
import com.rikka.mall.vo.CartVO;
import com.rikka.mall.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author Yuno
 * @time 3:26 PM 6/7/2023
 */

@RestController
@Slf4j
public class CartController {

    // TODO 测试接口
    // TODO controller层的url

    @Autowired
    private ICartService ics;

    @GetMapping("/carts")
    public ResponseVO<CartVO> showAll(HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.SESSION_USER_KEY);
        return ics.showAll(user.getId());
    }

    @PostMapping("/carts")
    public ResponseVO<CartVO> add(@Valid @RequestBody CartAddForm cartAddForm,
                                  HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.SESSION_USER_KEY);
        return ics.add(user.getId(), cartAddForm);
    }

    @PutMapping("/carts/{productId}")
    public ResponseVO<CartVO> update(@PathVariable Integer productId,
                                     @Valid @RequestBody CartUpdateForm form,
                                     HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.SESSION_USER_KEY);
        return ics.update(user.getId(), productId, form);
    }

    @DeleteMapping("/carts/{productId}")
    public ResponseVO<CartVO> delete(@PathVariable Integer productId,
                                     HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.SESSION_USER_KEY);
        return ics.delete(user.getId(), productId);
    }

    @PutMapping("/carts/selectAll")
    public ResponseVO<CartVO> selectAll(HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.SESSION_USER_KEY);
        return ics.selectAll(user.getId());
    }

    @PutMapping("/carts/unSelectAll")
    public ResponseVO<CartVO> unSelectAll(HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.SESSION_USER_KEY);
        return ics.unSelectAll(user.getId());
    }

    @GetMapping("/carts/products/sum")
    public ResponseVO<Integer> sum(HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.SESSION_USER_KEY);
        return ics.sum(user.getId());
    }

}
