package com.sea.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sea.common.exception.MyException;
import com.sea.dto.DishDto;
import com.sea.entity.Dish;
import com.sea.entity.DishFlavor;
import com.sea.service.DishFlavorService;
import com.sea.service.DishService;
import com.sea.mapper.DishMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 刘海洋
 * @description 针对表【dish(菜品管理)】的数据库操作Service实现
 * @createDate 2023-02-14 11:42:54
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
        implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品
     * 并将菜品口味保存
     *
     * @param dishDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveDishWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        boolean flag = false;
        flag = dishFlavorService.saveBatch(flavors);

        return flag;
    }

    /**
     * 查询菜品
     * 并显示菜品口味
     *
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> list = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(list);
        return dishDto;
    }

    /**
     * 修改菜品
     *
     * @param dishDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        boolean flag = false;
        dishFlavorService.remove(lambdaQueryWrapper);
        List<DishFlavor> flavors = dishDto.getFlavors();
        if (flavors != null) {
            flavors = flavors.stream().map((item) -> {

                item.setDishId(dishDto.getId());
                return item;
            }).collect(Collectors.toList());

            dishFlavorService.saveBatch(flavors);
        }

        return true;
    }

    @Override
    public boolean delete(List<Long> ids) {

        boolean flag = false;
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.in(Dish::getId, ids);
        lambdaQueryWrapper.eq(Dish::getStatus, 1);

        int count = this.count(lambdaQueryWrapper);
        if (count > 0) {
            throw new MyException("菜品正在售卖 不可删除 ");
        }
        this.removeByIds(ids);
        LambdaQueryWrapper<DishFlavor> dishFlavorQueryWrapper = new LambdaQueryWrapper();
        dishFlavorQueryWrapper.in(DishFlavor::getDishId, ids);
        dishFlavorService.remove(dishFlavorQueryWrapper);
        return true;

    }
}




