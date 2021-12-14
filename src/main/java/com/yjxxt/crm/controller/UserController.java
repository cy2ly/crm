package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.model.UserModel;
import com.yjxxt.crm.query.UserQuery;
import com.yjxxt.crm.service.UserService;

import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {
    @Resource
    private UserService userService;
    @PostMapping("login")
    @ResponseBody
    public ResultInfo userLogin (String userName, String userPwd) {
        ResultInfo resultInfo = new ResultInfo();
        // 调用Service层的登录方法，得到返回的用户对象
        UserModel userModel = userService.userLogin(userName, userPwd);
        /**
        * 登录成功后，有两种处理：
        * 1. 将用户的登录信息存入 Session （ 问题：重启服务器，Session 失效，客户端
        需要重复登录 ）
        * 2. 将用户信息返回给客户端，由客户端（Cookie）保存
        */
        // 将返回的UserModel对象设置到 ResultInfo 对象中qaz
        resultInfo.setResult(userModel);

        return resultInfo;
    }
    @PostMapping("updatePwd")
    @ResponseBody
    public ResultInfo updateUserPassword (HttpServletRequest request,
                                          String oldPassword, String newPassword,
                                          String confirmPwd) {
        ResultInfo resultInfo = new ResultInfo();
        // 获取userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 调用Service层的密码修改方法
        userService.changeUserPwd(userId, oldPassword, newPassword, confirmPwd);
        return resultInfo;
    }
    @RequestMapping("/toPasswordPage")
    public String toPasswordPage(){
        return "user/password";
    }

    @RequestMapping("/toSettingPage")
    public String toSettingPage(HttpServletRequest request){
        // 获取userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        User user = userService.selectByPrimaryKey(userId);
        request.setAttribute("user",user);
        return "user/setting";
    }

    @RequestMapping("setting")
    @ResponseBody
    public ResultInfo sayUpdate(User user) {
        ResultInfo resultInfo = new ResultInfo();
        //修改信息
        userService.updateByPrimaryKeySelective(user);
        //返回目标数据对象
        return resultInfo;
    }
    /**
     * 多条件查询用户数据
     * @param userQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryUserByParams(UserQuery userQuery) {
        return userService.findUserByParams(userQuery);
    }
    @RequestMapping("index")
    public String index(){
        return "user/user";
    }
    /**
     * 添加用户
     * @param user
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveUser(User user) {
        System.out.println(user.toString());
        userService.addUser(user);
        return success("用户添加成功！");
    }
    /**
     * 进入用户添加或更新页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("addOrUpdatePage")
    public String addUserPage(Integer id, Model model){
        if(null != id){
            model.addAttribute("user",userService.selectByPrimaryKey(id));
        }
        return "user/add_update";
    }

    /**
     * 删除用户
     * @param ids
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deletes(Integer[] ids) {
        userService.removeUserIds(ids);
        return success("用户删除成功");
    }
    @RequestMapping("sales")
    @ResponseBody
    public List<Map<String, Object>> findSales() {
        List<Map<String, Object>> list = userService.querySales();
        return list;
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user) {
        userService.changeUser(user);
        return success("用户更新成功！");
    }


}
