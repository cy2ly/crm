package com.yjxxt.crm.mapper;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.bean.SaleChance;
import com.yjxxt.crm.query.SaleChanceQuery;

public interface SaleChanceMapper extends BaseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SaleChance record);

    int insertSelective(SaleChance record);

    SaleChance selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SaleChance record);

    int updateByPrimaryKey(SaleChance record);

}