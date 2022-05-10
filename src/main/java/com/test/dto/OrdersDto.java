package com.test.dto;

import com.test.domain.front.OrderDetail;
import com.test.domain.front.Orders;
import lombok.Data;

import java.util.List;

/**
 * 订单类 dto
 */
@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;   // 收货人

    private List<OrderDetail> orderDetails; // 一个订单多个订单细节
}
