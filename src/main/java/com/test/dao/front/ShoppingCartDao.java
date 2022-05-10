package com.test.dao.front;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.domain.front.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartDao extends BaseMapper<ShoppingCart> {
}
