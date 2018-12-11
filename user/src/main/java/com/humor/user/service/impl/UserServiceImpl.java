package com.humor.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.humor.common.MD5Util;
import com.humor.domain.common.Const;
import com.humor.domain.common.ServerResponse;
import com.humor.domain.common.SnowFlakeIdGenerator;
import com.humor.domain.entity.User;
import com.humor.user.repository.UserRepository;
import com.humor.user.service.IUserService;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangshaoze
 */
@Service("iUserService")
@Transactional(rollbackFor = {Exception.class})
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SnowFlakeIdGenerator snowFlakeIdGenerator;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public ServerResponse<User> login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if(user==null ){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);
        if(!StringUtils.equals(md5Password,user.getPassword())){
            return ServerResponse.createByErrorMessage("密码错误");
        }
        Session session = (Session)entityManager.getDelegate();
        //游离态
        session.evict(user);
        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess("登录成功",user);
    }


    @Override
    public ServerResponse<User> register(User user,String sessionId){
        ServerResponse validResponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        validResponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        user.setId(snowFlakeIdGenerator.nextId());
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        User save = userRepository.save(user);
        Session session = (Session)entityManager.getDelegate();
        session.flush();
        session.evict(save);
        save.setPassword(StringUtils.EMPTY);
        if(save == null){
            return ServerResponse.createByErrorMessage("注册失败");
        }
        stringRedisTemplate.opsForValue().set(sessionId, JSON.toJSONString(save),Const.USER_EX_TIME,TimeUnit.SECONDS);
        return ServerResponse.createBySuccess("注册成功",save);
    }

    @Override
    public ServerResponse<String> checkValid(final String str, String type){
        if(StringUtils.isNotBlank(type)){
            //开始校验
            if(Const.USERNAME.equals(type)){
                long count = userRepository.count((root, query, cb) -> {
                    Expression<String> username = root.get("username").as(String.class);
                    Predicate predicate = cb.equal(username, str);
                    query.where(predicate);
                    return query.getRestriction();
                });
                if(count>0){
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if(Const.EMAIL.equals(type)){
                long count = userRepository.count((root, query, cb) -> {
                    Expression<String> email = root.get("email").as(String.class);
                    Predicate predicate = cb.equal(email, str);
                    query.where(predicate);
                    Predicate restriction = query.getRestriction();
                    return restriction;
                });
                if(count>0){
                    return ServerResponse.createByErrorMessage("email已存在");
                }
            }
        }else{
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    @Override
    public ServerResponse selectQuestion(String username){

        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        User user = userRepository.findByUsername(username);
        if(StringUtils.isNotBlank(user.getQuestion())){
            return ServerResponse.createBySuccess(user.getQuestion());
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }

    @Override
    public ServerResponse<String> checkAnswer(final String username, final String question, final String answer){
        long count = userRepository.count((root, query, cb) -> {
            Expression<String> username1 = root.get("username").as(String.class);
            Expression<String> question1 = root.get("question").as(String.class);
            Expression<String> answer1 = root.get("answer").as(String.class);
            Predicate predicate = cb.equal(username1, username);
            Predicate predicate1 = cb.equal(question1, question);
            Predicate predicate2 = cb.equal(answer1, answer);
            query.where(predicate, predicate1, predicate2);
            return query.getRestriction();
        });
        if(count>0){
            //说明问题及问题答案是这个用户的,并且是正确的
            String forgetToken = UUID.randomUUID().toString();
            stringRedisTemplate.opsForValue().set(Const.TOKEN_PREFIX+username,forgetToken,Const.USER_EX_TIME);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }


    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken){
        if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("参数错误,需要传递token");
        }
        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token = stringRedisTemplate.opsForValue().get(Const.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token无效或者过期");
        }

        if(StringUtils.equals(forgetToken,token)){
            String md5Password  = MD5Util.MD5EncodeUtf8(passwordNew);
            int count = userRepository.updatePassword(md5Password, username);
            if(count > 0){
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        }else{
            return ServerResponse.createByErrorMessage("token错误,请重新获取重置密码的token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }


    @Override
    public ServerResponse<String> resetPassword(final String passwordOld, String passwordNew,User user){
        //防止横向越权,要校验一下这个用户的旧密码,一定要指定是这个用户.因为我们会查询一个count(1),如果不指定id,那么结果就是true啦count>0;
        final long userId = user.getId();
        long count = userRepository.count((root, query, cb) -> {
            Expression<String> password = root.get("password").as(String.class);
            Expression<String> id = root.get("id").as(String.class);
            Predicate[] predicate = new Predicate[2];
            predicate[0]=cb.equal(password, MD5Util.MD5EncodeUtf8(passwordOld));
            predicate[1]=cb.equal(id, userId);
            query.where(predicate);
            return query.getRestriction();
        });
        if(count == 0){
            return ServerResponse.createByErrorMessage("旧密码错误");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        User save = userRepository.save(user);
        if(save!=null){
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }


    @Override
    public ServerResponse<User> updateInformation(User user){
        //username是不能被更新的
        //email也要进行一个校验,校验新的email是不是已经存在,并且存在的email如果相同的话,不能是我们当前的这个用户的.
        if(StringUtils.isNotBlank(user.getEmail())){
            User user1 = userRepository.findByEmailAndIdNot(user.getEmail(), user.getId());
            if(user1!=null){
                return ServerResponse.createByErrorMessage("email已存在,请更换email再尝试更新");
            }
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        User save = userRepository.save(updateUser);
        Session session = (Session) entityManager.getDelegate();
        session.evict(save);
        save.setPassword(StringUtils.EMPTY);
        if(save!=null){
            return ServerResponse.createBySuccess("更新个人信息成功",save);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }



    @Override
    public ServerResponse<User> getInformation(Long userId){
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        Session session = (Session) entityManager.getDelegate();
        session.evict(user);
        user.get().setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user.get());

    }


    /**
     * 校验是否是管理员
     * @param user
     * @return
     */
    @Override
    public ServerResponse checkAdminRole(User user){
        if(user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }



}
