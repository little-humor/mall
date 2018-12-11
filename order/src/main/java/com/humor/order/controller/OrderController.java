package com.humor.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.humor.common.CookieUtil;
import com.humor.common.JsonUtil;
import com.humor.domain.common.ResponseCode;
import com.humor.domain.common.ServerResponse;
import com.humor.domain.entity.User;
import com.humor.order.service.IOrderService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhangshaoze
 */
@Data
@Slf4j
@RestController
@RequestMapping("/order/")
public class OrderController {

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @PostMapping("create.do")
    public ServerResponse create(HttpServletRequest httpServletRequest, Long shippingId) throws JsonProcessingException {
        log.info("生成订单--------------------start--------------------");
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(loginToken==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }

        String userStr = stringRedisTemplate.opsForValue().get(loginToken);
        User user = JsonUtil.string2Obj(userStr,User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.createOrder(user.getId(),shippingId);
    }


    @DeleteMapping("cancel.do")
    public ServerResponse cancel(HttpServletRequest httpServletRequest, Long orderNo){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(loginToken==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String userStr = stringRedisTemplate.opsForValue().get(loginToken);
        User user = JsonUtil.string2Obj(userStr,User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.cancel(user.getId(),orderNo);
    }


    @GetMapping("get_order_cart_product.do")
    public ServerResponse getOrderCartProduct(HttpServletRequest httpServletRequest){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(loginToken==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String userStr = stringRedisTemplate.opsForValue().get(loginToken);
        User user = JsonUtil.string2Obj(userStr,User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderCartProduct(user.getId());
    }



    @GetMapping("detail.do")
    public ServerResponse detail(HttpServletRequest httpServletRequest,Long orderNo){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(loginToken==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String userStr = stringRedisTemplate.opsForValue().get(loginToken);
        User user = JsonUtil.string2Obj(userStr,User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderDetail(user.getId(),orderNo);
    }

    @GetMapping("list.do")
    public ServerResponse list(HttpServletRequest httpServletRequest, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(loginToken==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String userStr = stringRedisTemplate.opsForValue().get(loginToken);
        User user = JsonUtil.string2Obj(userStr,User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderList(user.getId(),pageNum,pageSize);
    }


    @GetMapping("query_order_pay_status.do")
    public ServerResponse<Boolean> queryOrderPayStatus(HttpServletRequest httpServletRequest, Long orderNo){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(loginToken==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String userStr = stringRedisTemplate.opsForValue().get(loginToken);
        User user = JsonUtil.string2Obj(userStr,User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }

        ServerResponse serverResponse = iOrderService.queryOrderPayStatus(user.getId(),orderNo);
        if(serverResponse.isSuccess()){
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }

}
