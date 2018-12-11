package com.humor.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhangshaoze
 */
@Data
public class CartVo {

    private List<CartProductVo> cartProductVoList;
    private BigDecimal cartTotalPrice;
    /**
     * 是否已经都勾选
     */
    private Boolean allChecked;
    private String imageHost;
}
