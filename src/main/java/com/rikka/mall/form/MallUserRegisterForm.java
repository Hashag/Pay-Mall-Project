package com.rikka.mall.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author Yuno
 * @time 5:34 PM 5/30/2023
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MallUserRegisterForm {

    @NotBlank(message = "用户名为空")
    private String username;

    @NotBlank(message = "密码为空")
    private String password;

    // TODO email 字段是必要的吗 ？
    @NotBlank(message = "邮箱为空")
    private String email;
}
