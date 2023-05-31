package com.sea.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sea.entity.DishFlavor;
import com.sea.service.DishFlavorService;
import com.sea.mapper.DishFlavorMapper;
import org.springframework.stereotype.Service;

/**
* @author 刘海洋
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service实现
* @createDate 2023-02-14 18:05:02
*/
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
    implements DishFlavorService{

}




