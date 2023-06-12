package com.rikka.mall.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rikka.mall.enums.ResponseStatusEnum;
import lombok.Data;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import java.util.Objects;

/**
 * @author Yuno
 * @time 4:25 PM 5/30/2023
 * 和前端页面交互数据
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseVO<T> {

    private T data;

    /**
     * @see com.rikka.mall.enums.ResponseStatusEnum code
     */
    private Integer status;
    private String msg;


    private ResponseVO(T data, int status, String msg) {
        this.data = data;
        this.status = status;
        this.msg = msg;
    }

    /**
     * @param data     返回的数据
     * @param extraMsg 附加消息
     * @return 返回给页面的响应
     */
    public static <T> ResponseVO<T> success(T data, String extraMsg) {
        return new ResponseVO<>(data, ResponseStatusEnum.SUCCESS.getCode(), StringUtils.isEmpty(extraMsg) ? ResponseStatusEnum.SUCCESS.getDesc() : extraMsg);
    }

    public static <T> ResponseVO<T> success(T data) {
        return success(data, null);
    }

    public static <T> ResponseVO<T> success() {
        return success(null, null);
    }

    public static <T> ResponseVO<T> success(String extraMsg) {
        return success(null, extraMsg);
    }

    /**
     * @param rse      响应状态
     * @param br       绑定错误信息
     * @param extraMsg 附加消息
     * @return 返回给页面的响应
     */
    public static <T> ResponseVO<T> error(ResponseStatusEnum rse, BindingResult br, String extraMsg) {
        return new ResponseVO<>(null, rse.getCode(),
                Objects.requireNonNull(br.getFieldError()).getField() + " " + br.getFieldError().getDefaultMessage() + ";" + (extraMsg == null ? "" : extraMsg));
    }

    public static <T> ResponseVO<T> error(ResponseStatusEnum rse, BindingResult br) {
        return error(rse, br, null);
    }


    /**
     * @param rse      响应状态
     * @param extraMsg 附加消息
     * @return 返回给页面的响应
     */
    public static <T> ResponseVO<T> error(ResponseStatusEnum rse, String extraMsg) {
        return new ResponseVO<>(null, rse.getCode(), extraMsg == null ? rse.getDesc() : extraMsg);
    }

    public static <T> ResponseVO<T> error(ResponseStatusEnum rse) {
        return error(rse, (String) null);
    }

}
