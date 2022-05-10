package com.test.controller.backend;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.common.Result;
import com.test.domain.backend.Category;
import com.test.domain.backend.Dish;
import com.test.domain.backend.DishFlavor;
import com.test.dto.DishDto;
import com.test.service.backend.CategoryService;
import com.test.service.backend.DishFlavorService;
import com.test.service.backend.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取菜品列表
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page> showList(int page, int pageSize, String name) {
        // 需要 dto 类把查出来的 dish 复制到 dishDto 中, 并查询 dishFlavor 加入 dishDto 中
        Page<Dish> dishPage = new Page<Dish>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<DishDto>();

        // 排序、查询条件
        LambdaQueryWrapper<Dish> qw = new LambdaQueryWrapper<>();
        qw.like(StringUtils.isNotBlank(name), Dish::getName, name)
          .orderByAsc(Dish::getCategoryId)
          .orderByDesc(Dish::getUpdateTime);

        dishService.page(dishPage, qw);

        // 复制页码, 排除 records 中的数据
        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");

        // 使用流进行 dishFlavor 查询和 records 中的数据复制
        LambdaQueryWrapper<DishFlavor> dfqw = new LambdaQueryWrapper<>();
        List<DishDto> dishDtoList = dishPage.getRecords().stream().map(item -> {

            // 根据 id 找出对应菜品口味
            Long dishFlavorId = item.getId();
            dfqw.eq(DishFlavor::getDishId, dishFlavorId);
            List<DishFlavor> list = dishFlavorService.list(dfqw);

            // 创建 dto 对象
            DishDto dishDto = new DishDto();
            dishDto.setFlavors(list);

            // 根据 id 找菜品分类 name
            Category category = categoryService.getById(item.getCategoryId());
            if(category != null) {
                dishDto.setCategoryName(category.getName());
            }

            // 复制数据
            BeanUtils.copyProperties(item, dishDto);

            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(dishDtoList);

        return Result.success(dishDtoPage);
    }

    /**
     * 添加菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public Result<String> addDish(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return Result.success("菜品添加成功！");
    }

    /**
     * 回显菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishDto> toUpdateDish(@PathVariable Long id) {
        // 获取菜品
        Dish dish = dishService.getById(id);

        // 获取菜品口味
        LambdaQueryWrapper<DishFlavor> qw = new LambdaQueryWrapper<>();
        qw.eq(DishFlavor::getDishId, id).eq(DishFlavor::getIsDeleted, 0);
        List<DishFlavor> dishFlavorList = dishFlavorService.list(qw);

        // 设置 dto
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(dishFlavorList);

        return Result.success(dishDto);
    }

    /**
     * 更新菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public Result<String> updateDish(@RequestBody DishDto dishDto) {

        dishService.updateWithFlavor(dishDto);
        return Result.success("更新菜品成功!");
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> deleteDish(@RequestParam List<Long> ids) {
        dishService.deleteWithFlavor(ids);

        return Result.success("删除菜品成功!");
    }

    @PostMapping("/status/{status}")
    public Result<String> setStatus(@PathVariable Integer status, Long[] ids) {
        log.info("批量启用或禁用 status----->{}  ids--------->{}", status, ids);
        // 使用 updateWrapper
        LambdaUpdateWrapper<Dish> uw = new LambdaUpdateWrapper<>();

        uw.set(ids != null, Dish::getStatus, status).in(Dish::getId, ids);

        dishService.update(uw);

        return Result.success((status == 1) ? "启用成功！" : "禁用成功");
    }

    /**
     * 根据 id 分别显示全部菜品
     * @param dish
     * @return
     */
    @GetMapping("/list")                        // categoryId
    public Result<List<DishDto>> selectAll(Dish dish) {
        // 查询菜品
        LambdaQueryWrapper<Dish> dqw = new LambdaQueryWrapper<>();
        dqw.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId())
          .eq(Dish::getStatus, 1) // 状态为起售
          .orderByAsc(Dish::getPrice);

        List<Dish> dishList = dishService.list(dqw);

        // 查询菜品口味
        LambdaQueryWrapper<DishFlavor> dfqw = new LambdaQueryWrapper<>();
        List<DishDto> dishDtoList = dishList.stream().map(item -> {
            dfqw.clear();

            dfqw.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> dishFlavors = dishFlavorService.list(dfqw);

            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            dishDto.setFlavors(dishFlavors);

            return dishDto;
        }).collect(Collectors.toList());

        return Result.success(dishDtoList);
    }
}
