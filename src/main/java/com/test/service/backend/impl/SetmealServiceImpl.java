package com.test.service.backend.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.dao.backend.SetmealDao;
import com.test.domain.backend.Setmeal;
import com.test.domain.backend.SetmealDish;
import com.test.dto.SetmealDto;
import com.test.exception.CustomException;
import com.test.service.backend.DishService;
import com.test.service.backend.SetmealDishService;
import com.test.service.backend.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealDao, Setmeal> implements SetmealService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        // 保存套餐
        this.save(setmealDto);
        Long setmealId = setmealDto.getId();

        // 保存套餐项
        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();

        // stream 流设置父 id
        setmealDishList = setmealDishList.stream().map(item -> {
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishList);
    }

    @Transactional
    @Override
    public void updateWithDish(SetmealDto setmealDto) {

        // 保存套餐
        this.updateById(setmealDto);

        // 删除套餐项
        LambdaQueryWrapper<SetmealDish> qw = new LambdaQueryWrapper<>();
        qw.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(qw);

        // 保存套餐项
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map(item -> {
            item.setSetmealId(setmealDto.getId());
            // 清空 id 防止未修改提交后 id 不变抛出已存在异常
            item.setId(null);

            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    @Transactional
    @Override
    public void deleteWithDish(List<Long> ids) {
        // 判断 status 是否停售, 停售才可以删除
        LambdaQueryWrapper<Setmeal> sqw = new LambdaQueryWrapper<>();
        sqw.eq(Setmeal::getStatus, 1)
           .in(Setmeal::getId, ids);

        if(this.count(sqw) > 0) {
            throw new CustomException("套餐正在售卖中，不能删除！");
        }

        // 删除套餐项
        LambdaQueryWrapper<SetmealDish> sdqw = new LambdaQueryWrapper<>();
        sdqw.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(sdqw);

        // 删除套餐
        this.removeBatchByIds(ids);
    }
}
