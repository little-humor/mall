package com.humor.admin.web;

import com.humor.admin.common.ServerResponse;
import com.humor.admin.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api("用户接口")
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "会员列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName",value = "会员名称",dataType = "String")
    })
    @GetMapping("list.do")
    public ServerResponse findAll(@RequestParam(value = "userName",required = false) String userName,
                                         @RequestParam(value = "page",required = false,defaultValue = "0")int page,
                                         @RequestParam(value = "limit",required = false,defaultValue = "10")int limit){
        return userService.findByUserName(userName,page==0?0:--page,limit);
    }

}
