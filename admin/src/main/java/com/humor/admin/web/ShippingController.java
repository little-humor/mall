package com.humor.admin.web;

import com.humor.admin.common.ServerResponse;
import com.humor.admin.service.IShippingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api("收货地址接口")
@RestController
@RequestMapping("shipping")
public class ShippingController {

    @Autowired
    private IShippingService shippingService;

    @ApiOperation(value = "收货地址列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "会员ID",dataType = "Long")
    })
    @GetMapping("list.do")
    public ServerResponse findAll(@RequestParam(value = "userId",required = false) long userId,
                                         @RequestParam(value = "page",required = false,defaultValue = "0")int page,
                                         @RequestParam(value = "limit",required = false,defaultValue = "10")int limit){
        return shippingService.findByUserId(userId,page==0?0:--page,limit);
    }

}
