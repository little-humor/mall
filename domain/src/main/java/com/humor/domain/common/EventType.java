package com.humor.domain.common;

/**
 * @author zhangshaoze
 * @create 2018-11-16 10:50 AM
 */
public enum EventType {

    /**
     * 创建订单事件
     */
    ORDER_CREATE,

    /**
     * 取消订单事件
     */
    ORDER_CANCEL,

    /**
     * 清空购物车
     */
    CLEAR_CART_BY_ID,

    /**
     * 减库存
     */

    SUBTRACT_STOCK

}
