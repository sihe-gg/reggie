package com.test.service.backend.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.dao.backend.DishFlavorDao;
import com.test.domain.backend.DishFlavor;
import com.test.service.backend.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorDao, DishFlavor> implements DishFlavorService{
}
