package com.test.dto;

import com.test.domain.backend.Setmeal;
import com.test.domain.backend.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
