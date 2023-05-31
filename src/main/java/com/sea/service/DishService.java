package com.sea.service;

import com.sea.dto.DishDto;
import com.sea.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 刘海洋
* @description 针对表【dish(菜品管理)】的数据库操作Service
* @createDate 2023-02-14 11:42:54
*/
public interface DishService extends IService<Dish> {
    public boolean saveDishWithFlavor(DishDto dishDto);
    public DishDto getByIdWithFlavor(Long id);
    public boolean updateWithFlavor(DishDto dishDto);

    public boolean delete(List<Long> ids);
}
