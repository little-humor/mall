package com.humor.user.service;

import com.humor.domain.common.ServerResponse;
import com.humor.domain.entity.User;

/**
 * @author zhangshaoze
 */
public interface IUserService {

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    ServerResponse<User> login(String username, String password);

    /**
     * 注册
     * @param user
     * @return
     */
    ServerResponse<User> register(User user,String sessionId);

    /**
     * 检查指定字段 值是否存在
     * @param str
     * @param type
     * @return
     */
    ServerResponse<String> checkValid(String str, String type);

    /**
     * 查询用户找回密码 提示问题
     * @param username
     * @return
     */
    ServerResponse selectQuestion(String username);

    /**
     * 验证提示问题答案正确性
     * @param username
     * @param question
     * @param answer
     * @return
     */
    ServerResponse<String> checkAnswer(String username, String question, String answer);

    /**
     * 忘记密码时重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    /**
     * 重置密码
     * @param passwordOld
     * @param passwordNew
     * @param user
     * @return
     */
    ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    ServerResponse<User> updateInformation(User user);

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    ServerResponse<User> getInformation(Long userId);

    /**
     * 查看用户角色
     * @param user
     * @return
     */
    ServerResponse checkAdminRole(User user);
}
