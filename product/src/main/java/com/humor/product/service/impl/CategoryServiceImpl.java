package com.humor.product.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.humor.domain.common.ServerResponse;
import com.humor.domain.common.SnowFlakeIdGenerator;
import com.humor.domain.entity.Category;
import com.humor.product.repository.CategoryRepository;
import com.humor.product.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author zhangshaoze
 */
@Slf4j
@Service("iCategoryService")
@Transactional(rollbackFor = {Exception.class})
public class CategoryServiceImpl implements ICategoryService {


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SnowFlakeIdGenerator snowFlakeIdGenerator;

    @Override
    public ServerResponse addCategory(String categoryName, Long parentId){
        if(parentId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }

        Category category = new Category();
        category.setId(snowFlakeIdGenerator.nextId());
        category.setName(categoryName);
        category.setParentId(parentId);
        //这个分类是可用的
        category.setStatus(true);
        Category save = categoryRepository.save(category);
        if(save!=null){
            return ServerResponse.createBySuccess("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    @Override
    public ServerResponse updateCategoryName(Long categoryId, String categoryName){
        if(categoryId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("更新品类参数错误");
        }
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if(!categoryOptional.isPresent()){
            return ServerResponse.createByErrorMessage("该品类不存在");
        }
        categoryOptional.get().setName(categoryName);
        Category save = categoryRepository.save(categoryOptional.get());
        if(save!=null){
            return ServerResponse.createBySuccess("更新品类名字成功");
        }
        return ServerResponse.createByErrorMessage("更新品类名字失败");
    }

    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Long categoryId){
        List<Category> categoryList = categoryRepository.findCategoryByParentId(categoryId);
        if(CollectionUtils.isEmpty(categoryList)){
            log.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }


    /**
     * 递归查询本节点的id及孩子节点的id
     * @param categoryId
     * @return
     */
    @Override
    public ServerResponse<List<Long>> selectCategoryAndChildrenById(Long categoryId){
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet,categoryId);


        List<Long> categoryIdList = Lists.newArrayList();
        if(categoryId != null){
            for(Category categoryItem : categorySet){
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }


    /**
     * 递归,算出子节点
     * @param categorySet
     * @param categoryId
     * @return
     */
    private Set<Category> findChildCategory(Set<Category> categorySet ,Long categoryId){
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if(categoryOptional.isPresent()){
            categorySet.add(categoryOptional.get());
        }
        //查找子节点,递归算法一定要有一个退出的条件
        List<Category> categoryList = categoryRepository.findCategoryByParentId(categoryId);
        for(Category categoryItem : categoryList){
            findChildCategory(categorySet,categoryItem.getId());
        }
        return categorySet;
    }






}
