package com.humor.cart.controller;

import com.humor.cart.service.ICartService;
import com.humor.common.CookieUtil;
import com.humor.common.JsonUtil;
import com.humor.domain.common.Const;
import com.humor.domain.common.ResponseCode;
import com.humor.domain.common.ServerResponse;
import com.humor.domain.entity.Cart;
import com.humor.domain.entity.User;
import com.humor.domain.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @author zhangshaoze
 */
@RestController
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @GetMapping("list.do")
    public ServerResponse<CartVo> list(HttpServletRequest httpServletRequest){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(loginToken==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String userStr = stringRedisTemplate.opsForValue().get(loginToken);
        User user = JsonUtil.string2Obj(userStr,User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.list(user.getId());
    }

    @PostMapping("add.do")
    public ServerResponse<CartVo> add(HttpServletRequest httpServletRequest, Integer count, Long productId){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(loginToken==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String userStr = stringRedisTemplate.opsForValue().get(loginToken);
        User user = JsonUtil.string2Obj(userStr,User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.add(user.getId(),productId,count);
    }



    @PutMapping("update.do")
    public ServerResponse<CartVo> update(HttpServletRequest httpServletRequest, Integer count, Long productId){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(loginToken==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String userStr = stringRedisTemplate.opsForValue().get(loginToken);
        User user = JsonUtil.string2Obj(userStr,User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.update(user.getId(),productId,count);
    }

    @DeleteMapping("delete_product.do")
    public ServerResponse<CartVo> deleteProduct(HttpServletRequest httpServletRequest, String productIds){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(loginToken==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String userStr = stringRedisTemplate.opsForValue().get(loginToken);
        User user = JsonUtil.string2Obj(userStr,User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.deleteProduct(user.getId(),productIds);
    }


    @RequestMapping("select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpServletRequest httpServletRequest){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(loginToken==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String userStr = stringRedisTemplate.opsForValue().get(loginToken);
        User user = JsonUtil.string2Obj(userStr,User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.CHECKED);
    }

    @RequestMapping("un_select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpServletRequest httpServletRequest){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(loginToken==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String userStr = stringRedisTemplate.opsForValue().get(loginToken);
        User user = JsonUtil.string2Obj(userStr,User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.UN_CHECKED);
    }



    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<CartVo> select(HttpServletRequest httpServletRequest, Long productId){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(loginToken==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String userStr = stringRedisTemplate.opsForValue().get(loginToken);
        User user = JsonUtil.string2Obj(userStr,User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.CHECKED);
    }

    @RequestMapping("un_select.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelect(HttpServletRequest httpServletRequest, Long productId){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(loginToken==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String userStr = stringRedisTemplate.opsForValue().get(loginToken);
        User user = JsonUtil.string2Obj(userStr,User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.UN_CHECKED);
    }



    @RequestMapping("get_cart_product_count.do")
    @ResponseBody
    public ServerResponse<Integer> getCartProductCount(HttpServletRequest httpServletRequest){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(loginToken==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String userStr = stringRedisTemplate.opsForValue().get(loginToken);
        User user = JsonUtil.string2Obj(userStr,User.class);
        if(user ==null){
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());
    }


    //服务间调用接口

    @GetMapping("checkedCartByUserId.do")
    public ServerResponse<List<Cart>> checkedCartByUserId(Long userId){
        return iCartService.checkedCartByUserId(userId);
    }

    @DeleteMapping("deleteCartById.do")
    public void deleteCartById(Long cartId){
        iCartService.deleteCartById(cartId);
    }

}
