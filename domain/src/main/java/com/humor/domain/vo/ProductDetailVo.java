package com.humor.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhangshaoze
 */
@Data
public class ProductDetailVo {

    private Long  id;
    private Long categoryId;
    private String name;
    private String subtitle;
    private String mainImage;
    private String subImages;
    private String detail;
    private BigDecimal price;
    private Integer stock;
    private Integer status;
    private String createTime;
    private String updateTime;

    private String imageHost;
    private Long parentCategoryId;
}
