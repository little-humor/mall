package com.humor.product.controller;

import com.humor.domain.common.ServerResponse;
import com.humor.domain.entity.Product;
import com.humor.product.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangshaoze
 */

@RestController
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    @GetMapping("list.do")
    public ServerResponse list(@RequestParam(value = "keyword",required = false)String keyword,
                                         @RequestParam(value = "categoryId",required = false)Long categoryId,
                                         @RequestParam(value = "pageNum",defaultValue = "0") int pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "15") int pageSize,
                                         @RequestParam(value = "orderBy",defaultValue = "") String orderBy){
        return iProductService.getProductByKeywordCategory(keyword,categoryId,pageNum,pageSize,orderBy);
    }


    /**
     * 服务间调用接口
     * @param productId
     * @return
     */
    @GetMapping("productInfoById.do")
    public ServerResponse<Product> productInfoById(Long productId){
        return iProductService.getProductDetail(productId);
    }



}
