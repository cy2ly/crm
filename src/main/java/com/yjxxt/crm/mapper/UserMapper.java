package com.yjxxt.crm.mapper;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.bean.User;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;


public interface UserMapper extends BaseMapper<User,Integer>{
//    int deleteByPrimaryKey(Integer id);
//
//    int insert(User record);
//
//    int insertSelective(User record);
//
//    User selectByPrimaryKey(Integer id);
//
//    int updateByPrimaryKeySelective(User record);
//
//    int updateByPrimaryKey(User record);
    // 根据用户名查询用户对象
    User queryUserByUserName(String userName);

    @MapKey("")
    List<Map<String,Object>> selectSales();

}