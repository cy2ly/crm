package com.yjxxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.CusDevPlan;
import com.yjxxt.crm.bean.SaleChance;
import com.yjxxt.crm.mapper.CusDevPlanMapper;
import com.yjxxt.crm.mapper.SaleChanceMapper;
import com.yjxxt.crm.query.CusDevPlanQuery;
import com.yjxxt.crm.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {
    @Resource
    private CusDevPlanMapper cusDevPlanMapper;
    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件查询计划项列表
     * @param cusDevPlanQuery
     * @return
     */
    public Map<String,Object> queryCusDevPlansByParams(CusDevPlanQuery cusDevPlanQuery){
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(cusDevPlanQuery.getPage(),cusDevPlanQuery.getLimit());
        PageInfo<CusDevPlan> pageInfo = new PageInfo<CusDevPlan>(selectByParams(cusDevPlanQuery));
        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveCusDevPlan(CusDevPlan cusDevPlan) {
        // 1. 参数校验
        checkParams(cusDevPlan.getSaleChanceId(),
                cusDevPlan.getPlanItem(),cusDevPlan.getPlanDate());
        // 2. 设置参数默认值
        cusDevPlan.setIsValid(1);
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setUpdateDate(new Date());
// 3. 执行添加，判断结果
        AssertUtil.isTrue(insertSelective(cusDevPlan) < 1, "计划项记录添加失败！");
    }
    /**
     * 验证参数
     * @param saleChanceId
     * @param planItem
     * @param planDate
     */
    private void checkParams(Integer saleChanceId, String planItem, Date planDate) {
        AssertUtil.isTrue(null == saleChanceId || saleChanceMapper.selectByPrimaryKey(saleChanceId) == null, "请设置营销机会ID！");
        AssertUtil.isTrue(StringUtils.isBlank(planItem), "请输入计划项内容！");
        AssertUtil.isTrue(null == planDate, "请指定计划项日期！");
    }
    /**
     * 更新计划项
     * 1.参数校验
     * id 非空 记录存在
     * 营销机会id 非空 记录必须存在
     * 计划项内容 非空
     * 计划项时间 非空
     * 2.参数默认值设置
     * updateDate
     * 3.执行更新 判断结果
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDevPlan(CusDevPlan cusDevPlan){
        AssertUtil.isTrue(null == cusDevPlan.getId() || null == selectByPrimaryKey(cusDevPlan.getId()),"待更新记录不存在!");
        // 1. 参数校验
        checkParams(cusDevPlan.getSaleChanceId(),cusDevPlan.getPlanItem(),cusDevPlan.getPlanDate());
        // 2. 设置参数默认值
        cusDevPlan.setUpdateDate(new Date());
        // 3. 执行添加，判断结果
        AssertUtil.isTrue(updateByPrimaryKeySelective(cusDevPlan)<1,"计划项记录更新失 败!");
    }
    /**
     * 删除计划项
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void delCusDevPlan(Integer id){
        CusDevPlan cusDevPlan =selectByPrimaryKey(id);
        AssertUtil.isTrue(null==id || null ==cusDevPlan ,"待删除记录不存在!");
        cusDevPlan.setIsValid(0);
        AssertUtil.isTrue(updateByPrimaryKeySelective(cusDevPlan)<1,"计划项记录删除失败!");
    }

}
