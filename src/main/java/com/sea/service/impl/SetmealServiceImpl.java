package com.sea.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sea.common.exception.MyException;
import com.sea.dto.SetmealDto;
import com.sea.entity.Setmeal;
import com.sea.entity.SetmealDish;
import com.sea.service.SetmealDishService;
import com.sea.service.SetmealService;
import com.sea.mapper.SetmealMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 刘海洋
* @description 针对表【setmeal(套餐)】的数据库操作Service实现
* @createDate 2023-02-14 11:42:54
*/
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
    implements SetmealService{
    @Autowired
    private SetmealDishService setmealDishService;
    @Override
    public boolean saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map(item->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        if(setmealDishService.saveBatch(setmealDishes)){
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(List<Long> ids) {

        boolean flag = false;
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.in(Setmeal::getId, ids);
        lambdaQueryWrapper.eq(Setmeal::getStatus, 1);

        int count = this.count(lambdaQueryWrapper);
        if (count > 0) {
            throw new MyException(" 套餐正在售卖 不可删除 ");
        }
        this.removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> dishFlavorQueryWrapper = new LambdaQueryWrapper();
        dishFlavorQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(dishFlavorQueryWrapper);
        return true;

    }

    @Override
    public SetmealDto getByIdWithDish(Long id) {
       Setmeal setmeal = this.getById(id);
       SetmealDto setmealDto = new SetmealDto();
       LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
       queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        BeanUtils.copyProperties(setmeal,setmealDto);
        setmealDto.setSetmealDishes(list);
        return setmealDto;


    }

    @Override
    @Transactional
    public boolean updateWithDish(SetmealDto setmealDto) {
        this.updateById(setmealDto);
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        boolean flag = false;
        setmealDishService.remove(lambdaQueryWrapper);
        List<SetmealDish> dishes = setmealDto.getSetmealDishes();
        if (dishes != null) {
            dishes = dishes.stream().map((item) -> {

                item.setSetmealId(setmealDto.getId());
                return item;
            }).collect(Collectors.toList());

            setmealDishService.saveBatch(dishes);
        }

        return true;
    }
}




