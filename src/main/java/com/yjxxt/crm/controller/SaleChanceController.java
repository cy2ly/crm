package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.SaleChance;
import com.yjxxt.crm.query.SaleChanceQuery;
import com.yjxxt.crm.service.SaleChanceService;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;
    @Resource
    private UserService userService;
    @RequestMapping("index")
    public String index(){
        return "saleChance/sale_chance";
    }
    /**
     * 多条件分页查询营销机会
     * @param query
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> querySaleChanceByParams (SaleChanceQuery query, Integer flag, HttpServletRequest request) {
        // 查询参数 flag=1 代表当前查询为开发计划数据，设置查询分配人参数
        if (null != flag && flag == 1) {
        // 获取当前登录用户的ID
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            query.setAssignMan(userId);
        }
        return saleChanceService.querySaleChanceByParams(query);
    }


    @RequestMapping("save")
    @ResponseBody
    public ResultInfo save(HttpServletRequest req,SaleChance saleChance){
        //获取登录用户的id
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        String trueName = userService.selectByPrimaryKey(userId).getTrueName();
        //创建人
        saleChance.setCreateMan(trueName);
        //添加操作
        saleChanceService.addSaleChance(saleChance);
        //返回目标对象
        return success("添加成功了");
    }
    /**
     * 机会数据添加与更新页面视图转发
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("addOrUpdateDialog")
    public String addOrUpdateSaleChancePage(Integer id, Model model){
        if (id != null){
            //通过主键查询
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            model.addAttribute("saleChance",saleChance);
        }
        return "saleChance/add_update";
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(SaleChance saleChance){
        //添加操作
        saleChanceService.updateSaleChance(saleChance);
        //返回目标对象
        return success("修改成功了");
    }
    /**
    * 批量删除
    * */
    @RequestMapping("del")
    @ResponseBody
    public ResultInfo deletes(Integer[] ids){
        //删除操作
        saleChanceService.removeSaleChanceIds(ids);
        //返回目标对象
        return success("批量删除成功了");
    }
    /**
     * 更新营销机会的开发状态
     * @param id
     * @param devResult
     * @return
     */
    @RequestMapping("updateSaleChanceDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDevResult(Integer id,Integer devResult){
        saleChanceService.updateSaleChanceDevResult(id,devResult);
        return success("开发状态更新成功！");
    }

}
