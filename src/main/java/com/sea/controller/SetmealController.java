package com.sea.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sea.common.R;
import com.sea.dto.DishDto;
import com.sea.dto.SetmealDto;
import com.sea.entity.Category;
import com.sea.entity.Dish;
import com.sea.entity.Setmeal;
import com.sea.entity.SetmealDish;
import com.sea.service.CategoryService;
import com.sea.service.SetmealDishService;
import com.sea.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.sun.webkit.perf.WCFontPerfLogger.log;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public R<Page<SetmealDto>> page(int page, int pageSize, String name) {
        Page setmealPage = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(name != null, Setmeal::getName, name);
        setmealLambdaQueryWrapper.orderByAsc(Setmeal::getPrice);

        setmealService.page(setmealPage,setmealLambdaQueryWrapper);

        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");

        List<Setmeal> records = setmealPage.getRecords();

        List<SetmealDto> list = records.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category != null){
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);

    }
    @PostMapping("/status/{stat}")
    public R<String> status(@PathVariable int stat, @RequestParam List<Long> ids) {

        LambdaUpdateWrapper<Setmeal> lambdaUpdateWrapper = new LambdaUpdateWrapper();
        lambdaUpdateWrapper.in(Setmeal::getId,ids);
        if (stat == 0) {
            lambdaUpdateWrapper.eq(Setmeal::getStatus,1);
            lambdaUpdateWrapper.set(Setmeal::getStatus,0);
            if(setmealService.update(null,lambdaUpdateWrapper)){
                return R.success("状态修改成功");
            }
            return R.error("状态修改失败");

        }else {
            lambdaUpdateWrapper.eq(Setmeal::getStatus,0);
            lambdaUpdateWrapper.set(Setmeal::getStatus,1);
            if(setmealService.update(null,lambdaUpdateWrapper)){
                return R.success("状态修改成功");
            }
            return R.error("状态修改失败");
        }
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        if (setmealService.delete(ids)) {
            return R.success("删除成功");
        }
        return R.error("删除失败 ");
    }
    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id){
        SetmealDto byIdWithDish = setmealService.getByIdWithDish(id);
        return R.success(byIdWithDish);
    }

    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto) {

        if (setmealService.updateWithDish(setmealDto)) {
            return R.success("修改成功");
        }
        return R.error("修改出错");
    }
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){

        if(setmealService.saveWithDish(setmealDto)){
            return R.success("添加成功");

        }
        return R.error("添加失败");
    }

    @GetMapping("/list")
    public R<List<Setmeal>> get(Setmeal setmeal){
       LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByAsc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

}
