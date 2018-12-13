package com.humor.admin.web;

import com.humor.admin.common.ServerResponse;
import com.humor.admin.service.ICartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author humor
 * @date 2018/12/13 9:01 PM
 */
@Api("购物车接口")
@RestController
@RequestMapping("cart")
public class CartController {

    @Autowired
    private ICartService cartService;

    @ApiOperation(value = "购物车列表",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId",value = "商品ID",dataType = "Long"),
            @ApiImplicitParam(name = "userId",value = "用户ID",dataType = "Long")
    })
    @GetMapping("list.do")
    public ServerResponse findAll(@RequestParam(value = "productId",required = false) Long productId,
                                            @RequestParam(value = "userId",required = false) Long userId,
                                            @RequestParam(value = "page",required = false,defaultValue = "0")int page,
                                            @RequestParam(value = "limit",required = false,defaultValue = "10")int limit){
        return cartService.findByProductIdAndUserId(productId,userId,page==0?0:--page,limit);
    }

}
