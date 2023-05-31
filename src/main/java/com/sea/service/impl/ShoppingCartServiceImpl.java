package com.sea.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sea.entity.ShoppingCart;
import com.sea.service.ShoppingCartService;
import com.sea.mapper.ShoppingCartMapper;
import org.springframework.stereotype.Service;

/**
* @author 刘海洋
* @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
* @createDate 2023-03-06 20:18:14
*/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService{

}




