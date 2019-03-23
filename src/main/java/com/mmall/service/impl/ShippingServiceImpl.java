package com.mmall.service.impl;

import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;
    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
       shipping.setUserId(userId);
       int rowCount=shippingMapper.insert(shipping);//keyProperty="id" 将自增的id 自动导入到shipping对象里
       if (rowCount>0){
           Map result= Maps.newHashMap();
           result.put("shippingId",shipping.getId()); //so这里有id
           return ServerResponse.createBySuccess("新建地址成功",result);
       }
       return ServerResponse.createByErrorMessage("新建地址失败");
    }

    @Override
    public ServerResponse<String> del(Integer userId, Integer shippingId) {
        int resultCount =shippingMapper.deleteByShippingIdByUserId(userId, shippingId);
        if (resultCount>0){
            return ServerResponse.createBySuccess("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");

    }
}
