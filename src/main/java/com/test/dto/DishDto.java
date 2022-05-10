package com.test.dto;

import com.test.domain.backend.Dish;
import com.test.domain.backend.DishFlavor;
import lombok.Data;

import java.util.List;

/**
 * web 和服务端之间交换数据对象类 data transfer obj
 */
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors;

    private String categoryName;

    private Integer copies;
}
