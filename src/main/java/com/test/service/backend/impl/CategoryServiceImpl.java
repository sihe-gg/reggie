package com.test.service.backend.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.dao.backend.CategoryDao;
import com.test.domain.backend.Category;
import com.test.domain.backend.Dish;
import com.test.domain.backend.Setmeal;
import com.test.exception.CustomException;
import com.test.service.backend.CategoryService;
import com.test.service.backend.DishService;
import com.test.service.backend.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishQW = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Setmeal> setMealQW = new LambdaQueryWrapper<>();

        // 查询当前分类是否关联了菜品，如果已经关联，抛出业务异常
        dishQW.eq(Dish::getCategoryId, id);
        long dishCount = dishService.count(dishQW);
        if (dishCount > 0) {
            throw new CustomException("当前分类下关联了菜品，不能删除！");
        }

        // 查询当前分类是否关联了套餐，如果已经关联，抛出业务异常
        setMealQW.eq(Setmeal::getCategoryId, id);
        long setMealCount = setmealService.count(setMealQW);
        if (setMealCount > 0) {
            throw new CustomException("当前分类下关联了套餐，不能删除！");
        }

        // 正常删除
        super.removeById(id);

    }
}
