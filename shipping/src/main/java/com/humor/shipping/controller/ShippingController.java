package com.humor.shipping.controller;

import com.humor.common.CookieUtil;
import com.humor.common.JsonUtil;
import com.humor.domain.common.ResponseCode;
import com.humor.domain.common.ServerResponse;
import com.humor.domain.entity.Shipping;
import com.humor.domain.entity.User;
import com.humor.shipping.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhangshaoze
 */

@RestController
@RequestMapping("/shipping/")
public class ShippingController {


    @Autowired
    private IShippingService iShippingService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @PostMapping("add.do")
    public ServerResponse add(HttpServletRequest httpServletRequest, Shipping shipping){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(loginToken==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String userStr = stringRedisTemplate.opsForValue().get(loginToken);
        User user = JsonUtil.string2Obj(userStr,User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.add(user.getId(),shipping);
    }


    @DeleteMapping("del.do")
    public ServerResponse del(HttpServletRequest httpServletRequest, Long shippingId){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(loginToken==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String userStr = stringRedisTemplate.opsForValue().get(loginToken);
        User user = JsonUtil.string2Obj(userStr,User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.del(user.getId(),shippingId);
    }

    @PutMapping("update.do")
    public ServerResponse update(HttpServletRequest httpServletRequest, Shipping shipping){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(loginToken==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String userStr = stringRedisTemplate.opsForValue().get(loginToken);
        User user = JsonUtil.string2Obj(userStr,User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.update(user.getId(),shipping);
    }


    @GetMapping("select.do")
    public ServerResponse<Shipping> select(HttpServletRequest httpServletRequest, Long shippingId){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(loginToken==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String userStr = stringRedisTemplate.opsForValue().get(loginToken);
        User user = JsonUtil.string2Obj(userStr,User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.select(user.getId(),shippingId);
    }


    @GetMapping("list.do")
    public ServerResponse<Page<Shipping>> list(@RequestParam(value = "pageNum",defaultValue = "0") int pageNum,
                                               @RequestParam(value = "pageSize",defaultValue = "10")int pageSize,
                                               HttpServletRequest httpServletRequest){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(loginToken==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String userStr = stringRedisTemplate.opsForValue().get(loginToken);
        User user = JsonUtil.string2Obj(userStr,User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.list(user.getId(),pageNum,pageSize);
    }


    /**
     * 服务间调用
     * @param userId
     * @param shippingId
     * @return
     */
    @GetMapping("shippingById.do")
    public ServerResponse<Shipping> shippingById(Long userId, Long shippingId){
        if(userId ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.select(userId,shippingId);
    }








}
