package com.sea.service;

import com.sea.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 刘海洋
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2023-03-20 11:19:35
*/
public interface OrdersService extends IService<Orders> {
        public void submit(Orders orders);
}
