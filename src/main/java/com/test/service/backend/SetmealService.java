package com.test.service.backend;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.domain.backend.Setmeal;
import com.test.dto.SetmealDto;

import java.util.List;

public interface SetmealService extends IService<Setmeal>{

    /**
     * 保存套餐和菜品
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);


    /**
     * 更新套餐和菜品
     * @param setmealDto
     */
    public void updateWithDish(SetmealDto setmealDto);


    /**
     * 更新套餐和菜品
     * @param ids
     */
    public void deleteWithDish(List<Long> ids);
}
