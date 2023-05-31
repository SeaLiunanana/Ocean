package com.sea.service;

import com.sea.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 刘海洋
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service
* @createDate 2023-02-14 09:12:55
*/
public interface CategoryService extends IService<Category> {
    public boolean remove(Long id);
    }

