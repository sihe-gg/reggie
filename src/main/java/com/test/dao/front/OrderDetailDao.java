package com.test.dao.front;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.domain.front.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailDao extends BaseMapper<OrderDetail> {
}
