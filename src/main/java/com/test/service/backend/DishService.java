package com.test.service.backend;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.domain.backend.Dish;
import com.test.dto.DishDto;

import java.util.List;

public interface DishService extends IService<Dish> {

    /**
     * 保存菜品和菜品口味
     * @param dishDto
     */
    public void saveWithFlavor(DishDto dishDto);

    /**
     * 修改菜品和菜品口味
     * @param dishDto
     */
    public void updateWithFlavor(DishDto dishDto);

    /**
     * 批量删除菜品和菜品口味
     * @param ids
     */
    public void deleteWithFlavor(List<Long> ids);
}
