package com.yjxxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.SaleChance;
import com.yjxxt.crm.mapper.SaleChanceMapper;
import com.yjxxt.crm.query.SaleChanceQuery;
import com.yjxxt.crm.utils.AssertUtil;
import com.yjxxt.crm.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {
    @Resource
    private SaleChanceMapper saleChanceMapper;
    /**
     * 多条件分页查询营销机会 (BaseService 中有对应的方法)
     * @param query
     * @return
     */
    public Map<String, Object> querySaleChanceByParams (SaleChanceQuery query) {
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(query.getPage(), query.getLimit());
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChanceMapper.selectByParams(query));
        map.put("code",0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance){
        //验证
        checkParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //state 0 ,1 （0--未分配，1--已经分配了）
        //未分配
        if(StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setState(0);
            saleChance.setDevResult(0);
        }
        //已经分配
        if(StringUtils.isNotBlank(saleChance.getAssignMan())){
            saleChance.setState(1);
            saleChance.setDevResult(1);
            saleChance.setAssignTime(new Date());
        }
        //设定默认值 state,devResult(0--未开发，1-开发中，2-开发成功了，3,-开发失败)
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        saleChance.setIsValid(1);
        //createDate,updateDate,分配时间
        //添加是否成功
        AssertUtil.isTrue(insertSelective(saleChance)<1,"添加失败了");
    }
    /**
     * 基本参数校验
     * @param customerName
     * @param linkMan
     * @param linkPhone
     */
    private void checkParams(String customerName, String linkMan, String linkPhone)
    {
        AssertUtil.isTrue(StringUtils.isBlank(customerName), "请输入客户名！");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan), "请输入联系人！");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone), "请输入手机号！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"手机号格式不正确！");
    }

    /**
     * 一:验证：
     *  1:当前对象的ID
     *  2.客户名非空
     *  3.联系人非空
     *  4.电话非空，11位合法
     * 二：
     *  设定默认值
     * 三
     *  修改是否成功
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance){
        //验证
        SaleChance temp = selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(temp==null,"待修改的记录不存在");
        checkParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //原来  未分配 state,devResult
        if(StringUtils.isBlank(temp.getAssignMan())&& StringUtils.isNotBlank(saleChance.getAssignMan())){
            saleChance.setState(1);
            saleChance.setDevResult(1);
            saleChance.setAssignTime(new Date());
        }
        //已经分配
        if(StringUtils.isNotBlank(temp.getAssignMan())&& StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setState(0);
            saleChance.setDevResult(0);
            saleChance.setAssignTime(null);
            saleChance.setAssignMan("");
        }
        //设定默认值
        saleChance.setUpdateDate(new Date());
        //修改是否成功
        AssertUtil.isTrue(updateByPrimaryKeySelective(saleChance)<1,"修改失败了");
    }


    /**
     * 批量删除
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeSaleChanceIds(Integer [] ids){
        //验证
        AssertUtil.isTrue(ids==null || ids.length==0,"请选择要删除的数据");
        //删除是否成功
        AssertUtil.isTrue(deleteBatch(ids)!=ids.length,"批量删除失败了");
    }

    /**
     * 更新营销机会的状态
     * 成功 = 2
     * 失败 = 3
     * @param id
     * @param devResult
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChanceDevResult(Integer id, Integer devResult) {
        AssertUtil.isTrue( null ==id,"待更新记录不存在!");
        SaleChance temp =selectByPrimaryKey(id);
        AssertUtil.isTrue( null ==temp,"待更新记录不存在!");
        temp.setDevResult(devResult);
        AssertUtil.isTrue(updateByPrimaryKeySelective(temp)<1,"机会数据更新失败!");
    }
}
