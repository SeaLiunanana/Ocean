package com.sea.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sea.entity.OrderDetail;
import com.sea.service.OrderDetailService;
import com.sea.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author 刘海洋
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2023-03-20 11:19:30
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}




