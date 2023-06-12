package com.rikka.mall.dao;

import com.rikka.mall.pojo.MallShipping;
import com.rikka.mall.pojo.MallShippingExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MallShippingMapper {
    int countByExample(MallShippingExample example);

    int deleteByExample(MallShippingExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MallShipping record);

    int insertSelective(MallShipping record);

    List<MallShipping> selectByExample(MallShippingExample example);

    MallShipping selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MallShipping record, @Param("example") MallShippingExample example);

    int updateByExample(@Param("record") MallShipping record, @Param("example") MallShippingExample example);

    int updateByPrimaryKeySelective(MallShipping record);

    int updateByPrimaryKey(MallShipping record);
}