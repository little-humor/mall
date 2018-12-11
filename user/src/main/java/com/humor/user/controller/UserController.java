package com.humor.user.controller;

import com.alibaba.fastjson.JSON;
import com.humor.common.CookieUtil;
import com.humor.common.JsonUtil;
import com.humor.domain.common.Const;
import com.humor.domain.common.ResponseCode;
import com.humor.domain.common.ServerResponse;
import com.humor.domain.entity.User;
import com.humor.user.service.IUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangshaoze
 */
@RestController
@RequestMapping("/user/")
public class UserController {


    @Autowired
    private IUserService iUserService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("login.do")
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse){
        System.out.println("----------------------------------------------");
        ServerResponse<User> response = iUserService.login(username,password);
            if(response.isSuccess()){
                //todo 注意事项：timeunit不指定时，取出来的字符串乱码（原因待分析）
                stringRedisTemplate.opsForValue().set(session.getId(),JSON.toJSONString(response.getData()),Const.USER_EX_TIME,TimeUnit.SECONDS);

                CookieUtil.writeLoginToken(httpServletResponse,session.getId());
            }
        return response;
    }

    @PostMapping("logout.do")
    public ServerResponse<String> logout(HttpServletRequest request,HttpServletResponse response){
        String loginToken = CookieUtil.readLoginToken(request);
        stringRedisTemplate.delete(loginToken);
        CookieUtil.delLoginToken(request,response);

        return ServerResponse.createBySuccess();
    }

    @PostMapping("register.do")
    public ServerResponse register(User user,HttpSession session,HttpServletResponse httpServletResponse){
        String sessionId = session.getId();
        ServerResponse response = iUserService.register(user,sessionId);
        if(response.isSuccess()){
            CookieUtil.writeLoginToken(httpServletResponse,sessionId);
        }

        return response;
    }


    @PostMapping("check_valid.do")
    public ServerResponse<String> checkValid(String str,String type){
        return iUserService.checkValid(str,type);
    }


    @PostMapping("get_user_info.do")
    public ServerResponse<User> getUserInfo(HttpServletRequest httpServletRequest){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(loginToken==null){
            ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
        }

        String userStr = stringRedisTemplate.opsForValue().get(loginToken);
        User user = JsonUtil.string2Obj(userStr,User.class);
        if(user != null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
    }


    @PostMapping("forget_get_question.do")
    public ServerResponse<String> forgetGetQuestion(String username){
        return iUserService.selectQuestion(username);
    }


    @PostMapping("forget_check_answer.do")
    public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
        return iUserService.checkAnswer(username,question,answer);
    }


    @PostMapping("forget_reset_password.do")
    public ServerResponse<String> forgetRestPassword(String username,String passwordNew,String forgetToken){
        return iUserService.forgetResetPassword(username,passwordNew,forgetToken);
    }



    @PostMapping("reset_password.do")
    public ServerResponse<String> resetPassword(HttpServletRequest httpServletRequest, String passwordOld, String passwordNew){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(loginToken==null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        String userStr = stringRedisTemplate.opsForValue().get(loginToken);
        User user = JsonUtil.string2Obj(userStr,User.class);
        if(user == null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(passwordOld,passwordNew,user);
    }


    @PostMapping("update_information.do")
    public ServerResponse<User> update_information(HttpServletRequest httpServletRequest, User user){

        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isBlank(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        String userStr = stringRedisTemplate.opsForValue().get(loginToken);
        if(StringUtils.isBlank(userStr)){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User currentUser = JsonUtil.string2Obj(userStr,User.class);
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        user.setPassword(currentUser.getPassword());
        ServerResponse<User> response = iUserService.updateInformation(user);
        if(response.isSuccess()){
            response.getData().setUsername(currentUser.getUsername());
            stringRedisTemplate.opsForValue().set(loginToken,JsonUtil.obj2String(response.getData()),Const.USER_EX_TIME);
        }
        return response;
    }

    @PostMapping("get_information.do")
    public ServerResponse<User> get_information(HttpServletRequest httpServletRequest){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(loginToken==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录,需要强制登录status=10");
        }
        String currentUserStr = stringRedisTemplate.opsForValue().get(loginToken);
        User currentUser = JsonUtil.string2Obj(currentUserStr,User.class);
        if(currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录,需要强制登录status=10");
        }
        return iUserService.getInformation(currentUser.getId());
    }

    @GetMapping("test")
    public void test(){
        System.out.println("--dddd--"+stringRedisTemplate.opsForValue().get("6522B7FA5F9A1A6CDCBD7218614B34AA"));
    }



}
