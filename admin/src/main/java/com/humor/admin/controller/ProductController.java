package com.humor.admin.controller;

import com.humor.admin.common.ServerResponse;
import com.humor.admin.entity.product.Category;
import com.humor.admin.entity.product.Product;
import com.humor.admin.repository.product.CategoryRepository;
import com.humor.admin.repository.product.ProductRepository;
import com.humor.admin.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhangshaoze
 * @date 2018/12/4 11:42 AM
 */
@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    /**
     * 一级分类
     * @return
     */
    @GetMapping("findParentCategory.do")
    public ServerResponse findParentCategory(){
        List<Category> all = categoryRepository.findCategoryByParentId(0);
        return ServerResponse.createBySuccess(all);
    }

    /**
     * 二级分类
     * @return
     */
    @GetMapping("findChildCategory.do")
    public ServerResponse findChildCategory(Integer categoryId){
        List<Category> all = categoryRepository.findCategoryByParentId(categoryId);
        return ServerResponse.createBySuccess(all);
    }

    /**
     * 商品列表
     * @param keyword 关键字
     * @param categoryId 所属分类
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("list.do")
    public ServerResponse list(@RequestParam(value = "keyword",required = false)String keyword,
                               @RequestParam(value = "categoryId",required = false)Integer categoryId,
                               @RequestParam(value = "page",defaultValue = "0") int page,
                               @RequestParam(value = "limit",defaultValue = "10") int limit){
        return productService.getProductByKeywordCategory(keyword,categoryId,page,limit);
    }

    /**
     * 添加/修改商品
     * @param product
     * @return
     */
    @PostMapping("addOrUpdate.do")
    public ServerResponse saveOrUpdate(Product product){
        return productService.saveOrUpdateProduct(product);
    }

    /**
     * 删除选中商品
     * @param id
     * @return
     */
    @DeleteMapping("deleteProducts")
    public ServerResponse deleteProduct(Integer id){
        productRepository.deleteById(id);
        return ServerResponse.createBySuccessMessage("删除成功");
    }

}
