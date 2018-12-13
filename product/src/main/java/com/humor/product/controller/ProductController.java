package com.humor.product.controller;

import com.humor.domain.common.ServerResponse;
import com.humor.domain.entity.Product;
import com.humor.product.service.IProductService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangshaoze
 */

@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    @ApiOperation(value = "商品列表",httpMethod = "GET",notes = "首页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword",  value = "商品名称", dataType = "String"),
            @ApiImplicitParam(name = "categoryId",  value = "商品分类id", dataType = "Long"),
            @ApiImplicitParam(name = "pageNum",  value = "默认：0", dataType = "int"),
            @ApiImplicitParam(name = "pageSize",  value = "默认：15", dataType = "int"),
            @ApiImplicitParam(name = "orderBy",  value = "功能待开发", dataType = "String")
    })
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
