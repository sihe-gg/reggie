package com.test.service.front;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.domain.front.Orders;

public interface OrdersService extends IService<Orders> {

    /**
     * 生成订单
     * @param orders
     */
    public void saveByEntity(Orders orders);
}
