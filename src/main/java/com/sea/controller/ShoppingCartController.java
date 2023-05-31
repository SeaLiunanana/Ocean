package com.sea.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sea.common.BaseContext;
import com.sea.common.R;
import com.sea.entity.ShoppingCart;
import com.sea.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /*
     * 增加菜品
     * */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> shoppingWrapper = new LambdaQueryWrapper<ShoppingCart>();
        shoppingWrapper.eq(ShoppingCart::getUserId, currentId);

        if (shoppingCart.getDishId() != null) {
            shoppingWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            shoppingWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartService.getOne(shoppingWrapper);
        if (one != null) {
            Integer number = one.getNumber();
            one.setNumber(number + 1);
            shoppingCartService.updateById(one);
        } else {
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            one = shoppingCart;
        }
        return R.success(one);


    }

    /*
     * 购物车查看
     * */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        LambdaQueryWrapper<ShoppingCart> lambdaWrapper = new LambdaQueryWrapper<>();
        lambdaWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        lambdaWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(lambdaWrapper);

        return R.success(list);

    }

    /*
    删除菜品

     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> shoppingWrapper = new LambdaQueryWrapper<ShoppingCart>();
        shoppingWrapper.eq(ShoppingCart::getUserId, currentId);

        if (shoppingCart.getDishId() != null) {
            shoppingWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            shoppingWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartService.getOne(shoppingWrapper);
        if (one != null) {
            Integer number = one.getNumber();
            one.setNumber(number - 1);
            if (one.getNumber() == 0) {
                shoppingCartService.removeById(one);
            } else {
                shoppingCartService.updateById(one);
            }
        } else {
            return R.error("无菜品可删除");
        }
        return R.success(one);


    }

    @DeleteMapping("/clean")
    public R<String> clean() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("删除成功");
    }

}
