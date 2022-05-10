package com.test.domain.front;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车
 */
@Data
public class ShoppingCart implements Serializable{

    private static final Long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Long userId;        // 用户id

    private Long dishId;        // 菜品id

    private Long setmealId;     // 套餐id

    private String dishFlavor;  // 口味

    private Integer number;     // 数量

    private BigDecimal amount;  // 金额

    private String image;

    private LocalDateTime createTime;
}
