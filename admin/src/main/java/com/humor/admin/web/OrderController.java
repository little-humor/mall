package com.humor.admin.web;

import com.humor.admin.common.ServerResponse;
import com.humor.admin.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api("订单接口")
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @ApiOperation(value = "会员列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "会员Id",dataType = "Long"),
            @ApiImplicitParam(name = "status",value = "订单状态",dataType = "Integer")
    })
    @GetMapping("list.do")
    public ServerResponse findAll(@RequestParam(value = "userId",required = false) Long userId,
                                         @RequestParam(value = "status",required = false) Integer status,
                                         @RequestParam(value = "page",required = false,defaultValue = "0")int page,
                                         @RequestParam(value = "limit",required = false,defaultValue = "10")int limit){
        return orderService.findAll(userId,status,page==0?0:--page,limit);
    }

}
