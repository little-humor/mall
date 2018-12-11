package com.humor.user.repository;

import com.humor.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author zhangshaoze
 */
public interface UserRepository extends JpaRepository<User,Long>,JpaSpecificationExecutor<User> {

    /**
     * 根据username查询user信息
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 校验email是否已存在，排除指定用户
     * @param email
     * @param userId
     * @return
     */
    User findByEmailAndIdNot(String email,Long userId);

    /**
     * 更新用户密码
     * @param password
     * @param username
     * @return
     */
    @Modifying
    @Query("update User set password = ?1 where username= ?2")
    int updatePassword(String password,String username);
}
