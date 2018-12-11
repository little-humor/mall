package com.humor.admin.service.impl;

import com.humor.admin.common.ResponseCode;
import com.humor.admin.common.ServerResponse;
import com.humor.admin.entity.product.Category;
import com.humor.admin.entity.product.Product;
import com.humor.admin.repository.product.CategoryRepository;
import com.humor.admin.repository.product.ProductRepository;
import com.humor.admin.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author zhangshaoze
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class ProductServiceImpl implements IProductService {


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        if (product != null) {
            if (!StringUtils.isEmpty(product.getSubImages())) {
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }
            //todo 商品状态后期处理 增加字典表！
            Product save = productRepository.save(product);
            if (save == null) {
                return ServerResponse.createByErrorMessage("提交失败");
            } else {
                return ServerResponse.createByErrorMessage("提交成功");
            }
        }
        return ServerResponse.createByErrorMessage("添加或更新产品参数不正确");
    }

    @Override
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        Product save = productRepository.save(product);
        if (save!=null) {
            return ServerResponse.createBySuccess("修改产品销售状态成功");
        }
        return ServerResponse.createByErrorMessage("修改产品销售状态失败");
    }


    @Override
    public ServerResponse getProductByKeywordCategory(String keyword, Integer categoryId, int page, int limit) {
        List<Integer> categoryIdList = new ArrayList();

        if (categoryId != null) {
            Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
            if (!categoryOptional.isPresent() && StringUtils.isEmpty(keyword)) {
                //没有该分类,并且还没有关键字,这个时候返回一个空的结果集,不报错
                return ServerResponse.createBySuccess();
            }
            categoryIdList = categoryRepository.findIdByParentId(categoryId);
            categoryIdList.add(categoryId);
        }
        Page<Product> productPage = null;
        //todo jpa中page是从0开始
        Pageable pageable = new PageRequest(page-1,limit,Sort.Direction.DESC,"createTime");
        //根据检索条件查询商品
        if(!StringUtils.isEmpty(keyword)&&categoryId!=null){
            //关键字+分类
            productPage = productRepository.findByNameLikeAndCategoryIdIn(keyword,categoryIdList,pageable);

        }else if(!StringUtils.isEmpty(keyword)){
            //关键字
            productPage = productRepository.findByNameLike(keyword,pageable);

        }else if(!CollectionUtils.isEmpty(categoryIdList)){
            //分类
            productPage = productRepository.findByCategoryIdIn(categoryIdList,pageable);
        }else{
            //所有
            productPage = productRepository.findAll(pageable);
        }

        ServerResponse<List<Product>> serverResponse = ServerResponse.createBySuccess(productPage.getContent());
        serverResponse.setCount(productPage.getTotalElements());
        return serverResponse;
    }


}
