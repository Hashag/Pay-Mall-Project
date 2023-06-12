package com.rikka.mall.exception;

import com.rikka.mall.enums.ResponseStatusEnum;
import com.rikka.mall.vo.ResponseVO;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Yuno
 * @time 7:36 PM 5/30/2023
 */

@ControllerAdvice
public class ExceptionHandler {


    /**
     * @param re 意外发生的运行时异常
     * @return 向前端返回的结果
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    @ResponseBody
//    @ResponseStatus
    public ResponseVO<Void> handler(RuntimeException re) {
        return ResponseVO.error(ResponseStatusEnum.SERVER_ERROR, re.getMessage());
    }


    /**
     * @param re 未登錄拋出的異常
     * @return 向前端返回的结果
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(NeedLoginException.class)
    @ResponseBody
    public ResponseVO<Void> handler(NeedLoginException re) {
        return ResponseVO.error(ResponseStatusEnum.NEED_LOGIN, re.getMessage());
    }


    /**
     * 处理表单参数注入异常
     * @param mane 表单参数注入异常
     * @return 异常响应
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseVO<Void> handler(MethodArgumentNotValidException mane) {
        return ResponseVO.error(ResponseStatusEnum.PARAM_ERROR, mane.getBindingResult());
    }

}
