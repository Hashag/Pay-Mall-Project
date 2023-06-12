package com.rikka.mall.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Yuno
 * @time 1:11 PM 5/31/2023
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MallCategoryVO {

    private Integer id;

    private Integer parentId;

    private String name;
    private Integer sortOrder;
    private List<MallCategoryVO> subCategories;
}
