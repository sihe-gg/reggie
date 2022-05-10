package com.test.domain.backend;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 菜品实体类
 */
@Data
public class Dish implements Serializable {

    private static final Long SerialVersionUID = 1L;

    private Long id;

    private String name;        // 菜品名称

    private Long categoryId;    // 菜品分类 id

    private BigDecimal price;   // 菜品价格

    private String code;        // 商品码

    private String image;       // 图片

    private String description; // 描述信息

    private Integer status;     // 0停售，1起售

    private Integer sort;       // 顺序

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    private Integer isDeleted;
}
