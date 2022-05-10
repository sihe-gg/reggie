package com.test.service.backend.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.dao.backend.SetmealDishDao;
import com.test.domain.backend.SetmealDish;
import com.test.service.backend.SetmealDishService;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishDao, SetmealDish> implements SetmealDishService{
}
