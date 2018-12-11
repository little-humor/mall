package com.humor.shipping.service.impl;

import com.google.common.collect.Maps;
import com.humor.domain.common.ServerResponse;
import com.humor.domain.common.SnowFlakeIdGenerator;
import com.humor.domain.entity.Shipping;
import com.humor.shipping.repository.ShippingRepository;
import com.humor.shipping.service.IShippingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author zhangshaoze
 */
@Slf4j
@Service("iShippingService")
@Transactional(rollbackFor = {Exception.class})
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingRepository shippingRepository;

    @Autowired
    private SnowFlakeIdGenerator snowFlakeIdGenerator;

    @Override
    public ServerResponse add(Long userId, Shipping shipping){
        shipping.setId(snowFlakeIdGenerator.nextId());
        shipping.setUserId(userId);
        Shipping save = shippingRepository.save(shipping);
        if(save!=null){
            Map result = Maps.newHashMap();
            result.put("shippingId",shipping.getId());
            return ServerResponse.createBySuccess("新建地址成功",result);
        }
        return ServerResponse.createByErrorMessage("新建地址失败");
    }

    @Override
    public ServerResponse<String> del(Long userId, Long shippingId){
        int resultCount = shippingRepository.deleteByIdAndUserId(shippingId,userId);

        if(resultCount > 0){
            return ServerResponse.createBySuccess("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }


    @Override
    /**
     * todo jpa更新数据 creattTime和updateTime如何处理
     */
    public ServerResponse update(Long userId, Shipping shipping){
        Shipping shipping1 = shippingRepository.findByIdAndUserId(shipping.getId(), userId);
        BeanUtils.copyProperties(shipping,shipping1,"createTime","updateTime","userId");
        Shipping save = shippingRepository.save(shipping1);
        if(save!=null){
            return ServerResponse.createBySuccess("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }

    @Override
    public ServerResponse<Shipping> select(Long userId, Long shippingId){
        Shipping shipping = shippingRepository.findByIdAndUserId(shippingId,userId);
        if(shipping == null){
            return ServerResponse.createByErrorMessage("无法查询到该地址");
        }
        return ServerResponse.createBySuccess("查询地址成功",shipping);
    }


    @Override
    public ServerResponse<Page<Shipping>> list(Long userId, int pageNum, int pageSize){
        Pageable pageable = new PageRequest(pageNum,pageSize,Sort.Direction.ASC,"id");
        Page<Shipping> shippingPage = shippingRepository.findByUserId(userId,pageable);
        return ServerResponse.createBySuccess(shippingPage);
    }



}
