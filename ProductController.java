package com.bruceliu.controller;

import com.bruceliu.pojo.Product;
import com.bruceliu.pojo.ProductInfo;
import com.bruceliu.pojo.Type;
import com.bruceliu.service.ProductService;
import com.bruceliu.utils.MessageResults;
import com.bruceliu.utils.PageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;
import java.util.jar.JarOutputStream;

/**
 * @BelongsProject: GoodsWarnSystem
 * @BelongsPackage: com.bruceliu.controller
 * @Author: bruceliu
 * @QQ:1241488705
 * @CreateTime: 2020-05-19 23:10
 * @Description: 商品的控制器
 */
@Controller
@Scope("prototype")
public class ProductController {

    @Resource
    ProductService productService;

    /**
     * 01-新增商品的方法
     *
     * @param product
     * @param file
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping("/product_add")
    public void addProduct(Product product, @RequestParam("file") MultipartFile file, HttpServletResponse response, HttpServletRequest request) throws Exception {
        response.setContentType("text/html;charset=utf-8");
        System.out.println("新增的对象是:" + product);
        if (file.isEmpty()) {
            response.getWriter().write("<script>alert('上传的图片不能为空!');location.href='page_product';</script>");
            return;
        }
        //上传的文件不为空
        String filename = file.getOriginalFilename();
        //获取文件的后缀
        String suffixName = filename.substring(filename.lastIndexOf("."));
        //生成一个新的文件名
        filename = UUID.randomUUID() + suffixName;
        System.out.println("要上传服务器的文件名是:" + filename);
        //获取文件上传的路径
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        File upload = new File(path.getAbsolutePath(), "/public/upload/" + filename);
        file.transferTo(upload);
        System.out.println("文件上传成功:" + upload.getAbsolutePath());

        product.setPimage(filename);
        int count = productService.addProduct(product);
        if (count > 0) {
            response.getWriter().write("<script>alert('新增成功!');location.href='product_page';</script>");
        } else {
            response.getWriter().write("<script>alert('新增失败!');location.href='product_page';</script>");
        }
    }

    /**
     * 02-商品分页
     *
     * @return
     */
    @RequestMapping("/product_page")
    public String queryPage(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize, Model model) {
        //总条数
        Integer totalCount = productService.totalCount();
        Map<String, Object> map = new HashMap<String, Object>();
        //开始查询的位置
        map.put("startIndex", (pageIndex - 1) * pageSize);
        map.put("pageSize", pageSize);
        //每页的数据
        List<Product> products = productService.getProductByPage(map);
        //封装数据
        PageUtils<Product> pageUtils = new PageUtils<Product>(pageIndex, pageSize, totalCount, products);
        System.out.println("分页工具类：" + pageUtils);
        model.addAttribute("pageUtils", pageUtils);
        System.out.println(pageUtils);
        return "product";
    }

    /**
     * 03-跳转更新页面
     *
     * @return
     */
    @RequestMapping("/product_goupdate")
    public String product_goupdate(Integer pid, Model model) {
        Product product = productService.findById(pid);
        model.addAttribute("product", product);
        return "editProduct";
    }

    /**
     * 04-更新商品的方法
     *
     * @param product
     * @param file
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping("/product_update")
    public void updateProduct(Product product, @RequestParam("file") MultipartFile file, HttpServletResponse response, HttpServletRequest request) throws Exception {
        response.setContentType("text/html;charset=utf-8");
        System.out.println("要更新的对象是:" + product);
        if (file.isEmpty() == false) {
            //上传的文件不为空
            String filename = file.getOriginalFilename();
            //获取文件的后缀
            String suffixName = filename.substring(filename.lastIndexOf("."));
            //生成一个新的文件名
            filename = UUID.randomUUID() + suffixName;
            System.out.println("要上传服务器的文件名是:" + filename);
            //获取文件上传的路径
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            File upload = new File(path.getAbsolutePath(), "/public/upload/" + filename);
            file.transferTo(upload);
            System.out.println("文件上传成功:" + upload.getAbsolutePath());
            product.setPimage(filename);
        }

        int count = productService.updateProduct(product);
        if (count > 0) {
            response.getWriter().write("<script>alert('更新成功!');location.href='product_page';</script>");
        } else {
            response.getWriter().write("<script>alert('更新失败!');location.href='product_goupdate?pid='" + product.getPid() + ";</script>");
        }
    }

    /**
     * 05-商品删除
     *
     * @param pid
     * @return
     */
    @ResponseBody
    @RequestMapping("/product_delete")
    public MessageResults product_delete(Integer pid) {
        Integer count = productService.deleteProduct(pid);
        MessageResults results = null;
        if (count > 0) {
            results = new MessageResults(200, "删除成功", null);
        } else {
            results = new MessageResults(500, "删除失败", null);
        }
        return results;
    }

    /**
     * 06-商品统计页面
     *
     * @return
     */
    @RequestMapping("/product_infos")
    public String product_infos(Model model) throws Exception {
        List<String> types=new ArrayList<String>();
        List<ProductInfo> productInfos = productService.getProductInfos();
        for (ProductInfo productInfo : productInfos) {
            types.add(productInfo.getName());
        }
        //把集合转为JSON格式的数据
        ObjectMapper mapper = new ObjectMapper();
        String jsonInfo = mapper.writeValueAsString(productInfos);
        System.out.println(jsonInfo);
        String typesJson = mapper.writeValueAsString(types);
        System.out.println(typesJson);
        model.addAttribute("jsonInfo",jsonInfo);
        model.addAttribute("typesJson",typesJson);
        return "productInfo";
    }

    /**
     * 07-根据分类ID查询商品的集合
     * @param tid
     * @return
     */
    @ResponseBody
    @RequestMapping("/productsByTid")
    public List<Product> productsByTid(Integer tid){
        return productService.productsByTid(tid);
    }

}
