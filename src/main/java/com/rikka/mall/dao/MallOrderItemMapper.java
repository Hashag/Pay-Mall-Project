package com.rikka.mall.dao;

import com.rikka.mall.pojo.MallOrderItem;
import com.rikka.mall.pojo.MallOrderItemExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MallOrderItemMapper {
    int countByExample(MallOrderItemExample example);

    int deleteByExample(MallOrderItemExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MallOrderItem record);

    int insertSelective(MallOrderItem record);

    int insertBatches(@Param("batches") List<MallOrderItem> batches);

    List<MallOrderItem> selectByExample(MallOrderItemExample example);

    MallOrderItem selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MallOrderItem record, @Param("example") MallOrderItemExample example);

    int updateByExample(@Param("record") MallOrderItem record, @Param("example") MallOrderItemExample example);

    int updateByPrimaryKeySelective(MallOrderItem record);

    int updateByPrimaryKey(MallOrderItem record);
}