package com.dai.mapper;

import com.dai.pojo.TbEverydayIncome;
import com.dai.pojo.TbEverydayIncomeExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbEverydayIncomeMapper {
    int countByExample(TbEverydayIncomeExample example);

    int deleteByExample(TbEverydayIncomeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbEverydayIncome record);

    int insertSelective(TbEverydayIncome record);

    List<TbEverydayIncome> selectByExample(TbEverydayIncomeExample example);

    TbEverydayIncome selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbEverydayIncome record, @Param("example") TbEverydayIncomeExample example);

    int updateByExample(@Param("record") TbEverydayIncome record, @Param("example") TbEverydayIncomeExample example);

    int updateByPrimaryKeySelective(TbEverydayIncome record);

    int updateByPrimaryKey(TbEverydayIncome record);
}