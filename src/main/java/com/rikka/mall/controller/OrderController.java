package com.rikka.mall.controller;

import com.github.pagehelper.PageInfo;
import com.rikka.mall.consts.MallConst;
import com.rikka.mall.form.OrderCreateForm;
import com.rikka.mall.pojo.MallUser;
import com.rikka.mall.service.IOrderService;
import com.rikka.mall.vo.OrderVO;
import com.rikka.mall.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author Yuno
 * @time 7:59 PM 6/11/2023
 */

@RestController
@Slf4j
public class OrderController {


    @Autowired
    private IOrderService ios;

    @PostMapping("/orders")
    public ResponseVO<OrderVO> create(@Valid @RequestBody OrderCreateForm form,
                                      HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.SESSION_USER_KEY);
        return ios.create(user.getId(), form.getShippingId());
    }

    @GetMapping("/orders")
    public ResponseVO<PageInfo> list(@RequestParam Integer pageNum,
                                     @RequestParam Integer pageSize,
                                     HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.SESSION_USER_KEY);
        return ios.list(user.getId(), pageNum, pageSize);
    }

    @GetMapping("/orders/{orderNo}")
    public ResponseVO<OrderVO> detail(@PathVariable("orderNo") Long orderNo,
                                      HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.SESSION_USER_KEY);
        return ios.detail(user.getId(), orderNo);
    }

    @PutMapping("/orders/{orderNo}")
    public ResponseVO cancel(@PathVariable Long orderNo,
                             HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.SESSION_USER_KEY);
        return ios.cancel(user.getId(), orderNo);
    }
}
