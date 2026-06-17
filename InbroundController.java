package com.bruceliu.controller;

import com.bruceliu.pojo.Inbround;
import com.bruceliu.pojo.Product;
import com.bruceliu.pojo.Type;
import com.bruceliu.pojo.User;
import com.bruceliu.service.InbroundService;
import com.bruceliu.service.ProductService;
import com.bruceliu.service.TypeService;
import com.bruceliu.utils.MessageResults;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @BelongsProject: GoodsWarnSystem
 * @BelongsPackage: com.bruceliu.controller
 * @CreateTime: 2020-06-10 23:32
 * @Description: TODO
 */
@Controller
@Scope("prototype")
public class InbroundController {

    @Resource
    InbroundService inbroundService;

    @Resource
    ProductService productService;

    @Resource
    TypeService typeService;

    /**
     * 入库方法
     * @param inbround
     * @return
     */
    @ResponseBody
    @RequestMapping("/bround_add")
    public MessageResults Inbround(Inbround inbround, HttpSession session){
        MessageResults results = null;
        try {
            User user= (User)session.getAttribute("userLogin");

            Product product = productService.findById(inbround.getTid());
            inbround.setName(product.getPtitle());

            Type type = typeService.getTypeById(inbround.getTid());
            inbround.setPtype(type.getTname());

            inbround.setInuser(user.getUname());

            System.out.println("入库的对象信息是："+inbround);
            int count = inbroundService.inbround(inbround);
            if (count > 0) {
                results = new MessageResults(200, "入库成功", null);
            } else {
                results = new MessageResults(500, "入库失败", null);
            }
        } catch (Exception e) {
            results = new MessageResults(500, "入库失败", null);
            e.printStackTrace();
        } finally {
        }
        return results;
    }

}
