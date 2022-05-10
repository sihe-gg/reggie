package com.test.service.backend.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.dao.backend.DishDao;
import com.test.domain.backend.Dish;
import com.test.domain.backend.DishFlavor;
import com.test.dto.DishDto;
import com.test.exception.CustomException;
import com.test.service.backend.DishFlavorService;
import com.test.service.backend.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishDao, Dish> implements DishService{

    @Autowired
    private DishFlavorService dishFlavorService;

    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        // 保存 dish 实体
        this.save(dishDto);
        Long dishId = dishDto.getId();

        // 获取 flavor
        List<DishFlavor> flavors = dishDto.getFlavors();

        // 保存 flavor 实体
        if(flavors != null) {
            // 循环设置 dishId
            flavors = flavors.stream().map(item -> {
               item.setDishId(dishId);
               return item;
            }).collect(Collectors.toList());

            dishFlavorService.saveBatch(flavors);
        }

    }

    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        // 修改菜品
        this.updateById(dishDto);
        Long dishId = dishDto.getId();

        // 删除菜品关联的口味
        LambdaQueryWrapper<DishFlavor> qw = new LambdaQueryWrapper<>();
        qw.eq(DishFlavor::getDishId, dishId);
        dishFlavorService.remove(qw);

        // 添加菜品口味
        List<DishFlavor> dishDtoFlavors = dishDto.getFlavors();
        dishDtoFlavors = dishDtoFlavors.stream().map(item -> {
            item.setId(null);
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(dishDtoFlavors);
    }

    @Transactional
    @Override
    public void deleteWithFlavor(List<Long> ids) {
        // 判断 status 是否在售卖
        LambdaQueryWrapper<Dish> dqw = new LambdaQueryWrapper<>();
        dqw.eq(Dish::getStatus, 1)
           .in(Dish::getId, ids);

        if(this.count(dqw) > 0) {
            throw new CustomException("该菜品正在售卖中，不能删除！");
        }

        // 删除菜品口味
        LambdaQueryWrapper<DishFlavor> dfqw = new LambdaQueryWrapper<>();
        dfqw.in(DishFlavor::getDishId, ids);
        dishFlavorService.remove(dfqw);


        // 删除菜品
        this.removeBatchByIds(ids);
    }

}
