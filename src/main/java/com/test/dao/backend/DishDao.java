package com.test.dao.backend;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.domain.backend.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishDao extends BaseMapper<Dish>{
}
