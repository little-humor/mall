package com.humor.shipping.repository;

import com.humor.domain.entity.Shipping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author zhangshaoze
 */
public interface ShippingRepository extends JpaRepository<Shipping,Long>,JpaSpecificationExecutor<Shipping> {

    /**
     * 根据收货地址id和用户id删除
     * @param shippingId
     * @param userId
     * @return
     */
    int deleteByIdAndUserId(Long shippingId,Long userId);

    /**
     * 根据用户id和收货地址id拉取地址信息
     * @param shippingId
     * @param userId
     * @return
     */
    Shipping findByIdAndUserId(Long shippingId,Long userId);

    /**
     * 用户地址列表展示
     * @param userId
     * @param pageable
     * @return
     */
    Page<Shipping> findByUserId(Long userId, Pageable pageable);

}
