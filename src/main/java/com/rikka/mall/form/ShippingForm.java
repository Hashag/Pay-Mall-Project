package com.rikka.mall.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Yuno
 * @time 9:57 AM 6/10/2023
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingForm {

    private String receiverName;

    private String receiverPhone;

    private String receiverMobile;

    private String receiverProvince;

    private String receiverCity;

    private String receiverDistrict;

    private String receiverAddress;

    private String receiverZip;

}
