package com.sea.service;

import com.sea.dto.DishDto;
import com.sea.dto.SetmealDto;
import com.sea.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 刘海洋
* @description 针对表【setmeal(套餐)】的数据库操作Service
* @createDate 2023-02-14 11:42:54
*/
public interface SetmealService extends IService<Setmeal> {

    public boolean saveWithDish(SetmealDto setmealDto);
    public boolean delete(List<Long> ids);
    public SetmealDto getByIdWithDish(Long id);
    public boolean updateWithDish(SetmealDto setmealDto);

}
