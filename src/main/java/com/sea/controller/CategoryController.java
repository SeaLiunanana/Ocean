package com.sea.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sea.common.R;
import com.sea.entity.Category;
import com.sea.entity.Dish;
import com.sea.entity.Employee;
import com.sea.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author 刘海洋
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody Category category){
        if (categoryService.save(category)) {
            return R.success("添加成功");
        }
        return R.error("添加失败");
    }

    @GetMapping("/page")
    public R<Page<Employee>> page(int page, int pageSize){
        Page infPage = new Page(page,pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(infPage,queryWrapper);
        return R.success(infPage);

    }
    @DeleteMapping
    public R<String> delete(Long ids){
        if(categoryService.remove(ids)){
            return R.success("删除成功");
        }return R.error("删除失败 ");
    }
    @PutMapping
    public R<String> update(@RequestBody Category category){
        if(categoryService.updateById(category)){
            return R.success("修改成功");
        }
        return R.error("修改失败");
    }

    @GetMapping("/list")
    public R<List<Category>> getType(Category category){
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        lambdaQueryWrapper.orderByAsc(Category::getSort);

        List<Category> list = categoryService.list(lambdaQueryWrapper);
        return R.success(list);
    }


}
