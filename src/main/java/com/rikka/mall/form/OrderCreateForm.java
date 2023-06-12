package com.rikka.mall.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Yuno
 * @time 8:02 PM 6/11/2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateForm {

    @NotNull
    private Integer shippingId;
}
