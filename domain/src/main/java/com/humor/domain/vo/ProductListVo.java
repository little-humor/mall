package com.humor.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhangshaoze
 */
@Data
public class ProductListVo {

    private Long id;
    private Long categoryId;

    private String name;
    private String subtitle;
    private String mainImage;
    private BigDecimal price;

    private Integer status;

    private String imageHost;

}
