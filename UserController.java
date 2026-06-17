package com.bruceliu.controller;

import com.bruceliu.pojo.Log;
import com.bruceliu.pojo.User;
import com.bruceliu.service.LogService;
import com.bruceliu.service.UserService;
import com.bruceliu.utils.DateUtils;
import com.bruceliu.utils.MessageResults;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @BelongsProject: GoodsWarnSystem
 * @BelongsPackage: com.bruceliu.controller
 * @Author: bruceliu
 * @QQ:1241488705
 * @CreateTime: 2020-05-10 17:51
 * @Description: TODO
 */
@Controller
@Scope("prototype")
public class UserController {

    @Resource
    UserService userService;

    @Resource
    LogService logService;

    /**
     * 01-ajax登录
     * @param user
     * @return
     */
    @PostMapping("/user_login")
    @ResponseBody
    public MessageResults login(User user, String ucode, String ip, String city, HttpSession session){
        System.out.println("登录ip是:"+ip);
        System.out.println("登录地址是:"+city);
        MessageResults reqults=null;
        //系统生成的验证码
        String checkcode=(String) session.getAttribute("checkcode");
        //校验系统生成的验证码
        if(checkcode.equalsIgnoreCase(ucode)){
            User userLogin = userService.userLogin(user);
            if(userLogin!=null){
                //用户的登录信息
                session.setAttribute("userLogin",userLogin);
                Log log=new Log(userLogin.getUname(), DateUtils.getNowTime(),ip,city);
                logService.addLog(log);

                reqults=new MessageResults(200,"登录成功",userLogin);
            }else{
                reqults=new MessageResults(500,"登录失败",null);
            }
        }else{
            reqults=new MessageResults(501,"验证码输入错误!",null);
        }
        return reqults;
    }

    /**
     * 02-用户退出
     * @return
     */
    @RequestMapping("/user_logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }

    /**
     * 03-查询用户的登录日志
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping("/user_getLogs")
    public MessageResults getLogs(HttpSession session){
        MessageResults reqults=null;
        User user= (User)session.getAttribute("userLogin");
        List<Log> logs = logService.getLogs(user.getUname());
        reqults=new MessageResults(200,"登录日志",logs);
        return reqults;
    }

}
