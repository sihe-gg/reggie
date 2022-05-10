package com.test.domain.front;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 订单
 */
@Data
public class Orders implements Serializable{
    private static final Long serialVersionUID = 1L;

    private Long id;

    private String number;              //订单号

    private Integer status;             // 1、待付款 2、待派送 3、已派送 4、已完成 5、已取消

    private Long userId;                // 下单用户id

    private Long addressBookId;         // 地址 id

    private LocalDateTime orderTime;    // 下单时间

    private LocalDateTime checkoutTime; // 结账时间

    private Integer payMethod;          // 支付方式 1、微信 2、支付宝

    private BigDecimal amount;          // 金额

    private String remark;                // 备注

    private String userName;

    private String phone;

    private String address;

    private String consignee;           // 收货人

}
