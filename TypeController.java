package com.bruceliu.controller;

import com.bruceliu.pojo.Type;
import com.bruceliu.service.TypeService;
import com.bruceliu.utils.MessageResults;
import com.bruceliu.utils.PageUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: GoodsWarnSystem
 * @BelongsPackage: com.bruceliu.controller
 * @Author: bruceliu
 * @QQ:1241488705
 * @CreateTime: 2020-05-12 22:48
 * @Description: TODO
 */
@Controller
@Scope("prototype")
public class TypeController {

    @Resource
    TypeService typeService;

    /**
     * 01-新增商品列别
     * @param type
     * @return
     */
    @RequestMapping("/type_add")
    @ResponseBody
    public MessageResults addType(Type type){
        System.out.println("要新增的type对象是:"+type);
        MessageResults results=null;
        Integer count = typeService.addType(type);
        if(count>0){
            results=new MessageResults(200,"新增成功",null);
        }else{
            results=new MessageResults(500,"新增失败",null);
        }
        return results;
    }

    /**
     * 02-分类分页
     * @return
     */
    @RequestMapping("/type_page")
    public String queryPage(@RequestParam(value = "pageIndex",defaultValue = "1") Integer pageIndex,
                            @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize, Model model){
        //总条数
        Integer totalCount = typeService.getTotalCount();
        Map<String,Object> map=new HashMap<String,Object>();
        //开始查询的位置
        map.put("startIndex",(pageIndex-1)*pageSize);
        map.put("pageSize",pageSize);
        //每页的数据
        List<Type> types = typeService.getTypesByPage(map);
        //封装数据
        PageUtils<Type> pageUtils=new PageUtils<Type>(pageIndex,pageSize,totalCount,types);
        System.out.println("分页工具类："+pageUtils);
        model.addAttribute("pageUtils",pageUtils);
        System.out.println(pageUtils);
        return "type";
    }

    /**
     * 03-类别删除
     * @param tid
     * @return
     */
    @ResponseBody
    @RequestMapping("/type_delete")
    public MessageResults delete(Integer tid){
        Integer count = typeService.deleteType(tid);
        MessageResults results=null;
        if(count>0){
            results=new MessageResults(200,"删除成功",null);
        }else{
            results=new MessageResults(500,"删除失败",null);
        }
        return results;
    }

    /**
     * 04-跳转更新
     * @param tid
     * @param model
     * @return
     */
    @RequestMapping("/type_getById")
    public String toupdate(Integer tid,Model model){
        Type type = typeService.getTypeById(tid);
        model.addAttribute("type",type);
        System.out.println("要更新的对象是:"+type);
        return "editType";
    }

    /**
     * 05-更新
     * @return
     */
    @ResponseBody
    @RequestMapping("/type_update")
    public MessageResults update(Type type){
        System.out.println("要更新的对象是："+type);
        MessageResults results=null;
        try {
            int count = typeService.updateType(type);
            if(count>0){
                results=new MessageResults(200,"更新成功",null);
            }else{
                results=new MessageResults(500,"更新失败",null);
            }
        } catch (Exception e) {
            results=new MessageResults(500,"更新失败",null);
            e.printStackTrace();
        } finally {
        }
        return results;
    }

    /**
     * 06-返回商品类别的JSON数据
     * @return
     */
    @ResponseBody
    @RequestMapping("/type_all")
    public List<Type> findTypes(){
        return typeService.getTypes();
    }
}
