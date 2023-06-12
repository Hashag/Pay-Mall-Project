package com.rikka.mall.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Yuno
 * @time 10:09 PM 6/7/2023
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartUpdateForm {

    private Integer quantity;

    private Boolean selected;

}
