package com.test.domain.front;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单详细
 */
@Data
public class OrderDetail implements Serializable{

    private static final Long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Long dishId;                // 菜品id

    private Long orderId;               // 订单id

    private Long setmealId;             // 套餐id

    private String dishFlavor;          // 菜品口味

    private Integer number;             // 数量

    private BigDecimal amount;          // 金额

    private String image;
}
