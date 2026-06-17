package com.bruceliu.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @BelongsProject: GoodsWarnSystem
 * @BelongsPackage: com.bruceliu.controller
 * @Author: bruceliu
 * @QQ:1241488705
 * @CreateTime: 2020-05-10 18:53
 * @Description: TODO
 */
@Controller
@Scope("prototype")
public class CommonController {

    /**
     * 00-进入登录页面
     * @return
     */
    @RequestMapping(value = {"/","/login"})
    public String toLogin(){
        return "login";
    }

    /**
     * 01-通用的后台跳转模板页面方法
     * @param pageName
     * @return
     */
    @RequestMapping("/page_{pageName}")
    public String toPage(@PathVariable("pageName") String pageName){
        //跳转到模板引擎页面上
        return pageName;
    }

}
