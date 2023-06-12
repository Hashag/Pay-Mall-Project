package com.rikka.mall.controller;

import com.rikka.mall.consts.MallConst;
import com.rikka.mall.form.ShippingForm;
import com.rikka.mall.pojo.MallUser;
import com.rikka.mall.service.IShippingService;
import com.rikka.mall.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author Yuno
 * @time 2:11 PM 6/12/2023
 */

@RestController
public class ShippingController {

    @Autowired
    private IShippingService shippingService;

    @PostMapping("/shippings")
    public ResponseVO add(@Valid @RequestBody ShippingForm form,
                          HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.SESSION_USER_KEY);
        return shippingService.add(user.getId(), form);
    }

    @DeleteMapping("/shippings/{shippingId}")
    public ResponseVO delete(@PathVariable Integer shippingId,
                             HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.SESSION_USER_KEY);
        return shippingService.delete(user.getId(), shippingId);
    }

    @PutMapping("/shippings/{shippingId}")
    public ResponseVO update(@PathVariable Integer shippingId,
                             @Valid @RequestBody ShippingForm form,
                             HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.SESSION_USER_KEY);
        return shippingService.update(user.getId(), shippingId, form);
    }

    @GetMapping("/shippings")
    public ResponseVO list(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                           @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                           HttpSession session) {
        MallUser user = (MallUser) session.getAttribute(MallConst.SESSION_USER_KEY);
        return shippingService.list(user.getId(), pageNum, pageSize);
    }
}
