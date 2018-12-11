package com.humor.product.service.impl;

import com.google.common.collect.Lists;
import com.humor.common.DateTimeUtil;
import com.humor.domain.common.Const;
import com.humor.domain.common.ResponseCode;
import com.humor.domain.common.ServerResponse;
import com.humor.domain.entity.Category;
import com.humor.domain.entity.Product;
import com.humor.domain.vo.ProductDetailVo;
import com.humor.domain.vo.ProductListVo;
import com.humor.product.repository.CategoryRepository;
import com.humor.product.repository.ProductRepository;
import com.humor.product.service.IProductService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author zhangshaoze
 */
@Service("iProductService")
@Transactional(rollbackFor = {Exception.class})
public class ProductServiceImpl implements IProductService {


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());
        //图片地址
        productDetailVo.setImageHost("");

        Optional<Category> optionalCategory = categoryRepository.findById(product.getCategoryId());
        if (!optionalCategory.isPresent()) {
            //默认根节点
            productDetailVo.setParentCategoryId(0L);
        } else {
            productDetailVo.setParentCategoryId(optionalCategory.get().getParentId());
        }

        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }

    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost("");
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }

    @Override
    public ServerResponse<Product> getProductDetail(Long productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent()) {
            return ServerResponse.createByErrorMessage("产品不存在");
        }
        if (optionalProduct.get().getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
            return ServerResponse.createByErrorMessage("产品已下架");
        }
        return ServerResponse.createBySuccess(optionalProduct.get());
    }


    @Override
    public ServerResponse getProductByKeywordCategory(String keyword, Long categoryId, int pageNum, int pageSize, String orderBy) {
        List<Long> categoryIdList = new ArrayList<Long>();

        if (categoryId != null) {
            Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
            if (!categoryOptional.isPresent() && StringUtils.isBlank(keyword)) {
                //没有该分类,并且还没有关键字,这个时候返回一个空的结果集,不报错
                return ServerResponse.createBySuccess();
            }
            categoryIdList = categoryRepository.findIdByParentId(categoryId);
        }
        Page<Product> productList = null;
        Pageable pageable = new PageRequest(pageNum,pageSize,Sort.Direction.DESC,"createTime");
        //排序处理
        if(StringUtils.isNotBlank(keyword)&&categoryId!=null){
            //关键字与分类都有值时
            productList = productRepository.findByNameLikeAndCategoryIdIn(keyword,categoryIdList,pageable);

        }else if(StringUtils.isNotBlank(keyword)){
            //指定关键字
            productList = productRepository.findByNameLike(keyword,pageable);

        }else if(!CollectionUtils.isEmpty(categoryIdList)){
            //分类
            productList = productRepository.findByCategoryIdIn(categoryIdList,pageable);
        }else{
            //首页
            productList = productRepository.findAll(pageable);
        }

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product product : productList) {
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }

        return ServerResponse.createBySuccess(productListVoList);
    }




}
