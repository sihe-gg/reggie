package com.test.dao.front;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.domain.front.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersDao extends BaseMapper<Orders> {
}
