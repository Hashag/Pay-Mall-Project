package com.rikka.mall.dao;

import com.rikka.mall.pojo.MallProduct;
import com.rikka.mall.pojo.MallProductExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MallProductMapper {
    int countByExample(MallProductExample example);

    int deleteByExample(MallProductExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MallProduct record);

    int insertSelective(MallProduct record);

    List<MallProduct> selectByExample(MallProductExample example);

    MallProduct selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MallProduct record, @Param("example") MallProductExample example);

    int updateByExample(@Param("record") MallProduct record, @Param("example") MallProductExample example);

    int updateByPrimaryKeySelective(MallProduct record);

    int updateByPrimaryKey(MallProduct record);
}