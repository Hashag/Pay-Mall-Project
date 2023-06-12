package com.rikka.mall.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author Yuno
 * @time 10:24 PM 5/30/2023
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MallUserLoginForm {

    @NotBlank(message = "用户名为空")
    private String username;

    @NotBlank(message = "密码为空")
    private String password;
}
