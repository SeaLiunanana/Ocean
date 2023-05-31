package com.sea.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sea.common.R;
import com.sea.dto.DishDto;
import com.sea.entity.Category;
import com.sea.entity.Dish;
import com.sea.entity.DishFlavor;
import com.sea.entity.Employee;
import com.sea.service.CategoryService;
import com.sea.service.DishFlavorService;
import com.sea.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public R<Page<DishDto>> page(int page, int pageSize, String name) {
        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);

    }

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        if (dishService.saveDishWithFlavor(dishDto)) {
            return R.success("添加成功");
        }
        return R.error("添加出错");
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {

        if (dishService.updateWithFlavor(dishDto)) {
            return R.success("修改成功");
        }
        return R.error("修改出错");
    }

    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id) {
        DishDto byIdWithFlavor = dishService.getByIdWithFlavor(id);
        return R.success(byIdWithFlavor);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        if (dishService.delete(ids)) {
            return R.success("删除成功");
        }
        return R.error("删除失败 ");
    }

    @PostMapping("/status/{stat}")
    public R<String> status(@PathVariable int stat, @RequestParam List<Long> ids) {

        LambdaUpdateWrapper<Dish> dishLambdaUpdateWrapper = new LambdaUpdateWrapper();
        dishLambdaUpdateWrapper.in(Dish::getId,ids);
        if (stat == 0) {
            dishLambdaUpdateWrapper.eq(Dish::getStatus,1);
            dishLambdaUpdateWrapper.set(Dish::getStatus,0);
            if(dishService.update(null,dishLambdaUpdateWrapper)){
                return R.success("状态修改成功");
            }
            return R.error("状态修改失败");

        }else {
            dishLambdaUpdateWrapper.eq(Dish::getStatus,0);
            dishLambdaUpdateWrapper.set(Dish::getStatus,1);
            if(dishService.update(null,dishLambdaUpdateWrapper)){
                return R.success("状态修改成功");
            }
            return R.error("状态修改失败");
        }
    }
    /*
    * 用户菜品单独查询
    *
    * */
    @GetMapping("/list")
    public R<List<DishDto>> getType(Dish dish){
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        lambdaQueryWrapper.orderByAsc(Dish::getSort);
        lambdaQueryWrapper.eq(Dish::getStatus,1);

        lambdaQueryWrapper.like(dish.getName() != null,Dish::getName,dish.getName());

        List<Dish> list = dishService.list(lambdaQueryWrapper);

        List<DishDto> dishDtos = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            dishDto = dishService.getByIdWithFlavor(item.getId());


            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtos);
    }


}
