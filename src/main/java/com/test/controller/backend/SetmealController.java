package com.test.controller.backend;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.common.Result;
import com.test.domain.backend.Category;
import com.test.domain.backend.Setmeal;
import com.test.domain.backend.SetmealDish;
import com.test.dto.SetmealDto;
import com.test.service.backend.CategoryService;
import com.test.service.backend.SetmealDishService;
import com.test.service.backend.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    @GetMapping("/page")
    public Result<Page> showList(int page, int pageSize, String name) {
        Page<Setmeal> setmealPage = new Page<Setmeal>();
        Page<SetmealDto> setmealDtoPage = new Page<SetmealDto>();

        // 查询 setmeal
        LambdaQueryWrapper<Setmeal> sqw = new LambdaQueryWrapper<>();
        sqw.like(StringUtils.isNotBlank(name), Setmeal::getName, name)
           .orderByAsc(Setmeal::getCategoryId)
           .orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(setmealPage, sqw);

        // 复制页码
        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");

        List<Setmeal> setmealList = setmealPage.getRecords();

        LambdaQueryWrapper<SetmealDish> sdqw = new LambdaQueryWrapper<>();
        List<SetmealDto> setmealDtos = setmealList.stream().map(item -> {
            // 查询 setmealDto.categoryName
            Category category = categoryService.getById(item.getCategoryId());

            // 查询 setmealDto.setmealDish 列表
            sdqw.clear();
            sdqw.eq(SetmealDish::getSetmealId, item.getId());
            List<SetmealDish> setmealDishes = setmealDishService.list(sdqw);

            SetmealDto setmealDto = new SetmealDto();
            setmealDto.setSetmealDishes(setmealDishes);
            setmealDto.setCategoryName(category.getName());

            BeanUtils.copyProperties(item, setmealDto);

            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(setmealDtos);

        return Result.success(setmealDtoPage);
    }

    /**
     * 添加套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public Result<String> addSetmeal(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDish(setmealDto);

        return Result.success("套餐保存成功！");
    }


    /**
     * 回显套餐
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Result<SetmealDto> toUpdateSetmeal(@PathVariable Long id) {
        // 获取套餐
        Setmeal setmeal = setmealService.getById(id);

        // 获取套餐项
        LambdaQueryWrapper<SetmealDish> qw = new LambdaQueryWrapper<>();
        qw.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishList = setmealDishService.list(qw);

        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        setmealDto.setSetmealDishes(setmealDishList);

        return Result.success(setmealDto);
    }

    /**
     * 更新套餐
     * @param setmealDto
     * @return
     */
    @PutMapping
    public Result<String> updateSetmeal(@RequestBody SetmealDto setmealDto) {
        setmealService.updateWithDish(setmealDto);

        return Result.success("套餐修改成功！");
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> deleteSetmeal(@RequestParam List<Long> ids) {
        setmealService.deleteWithDish(ids);

        return Result.success("套餐删除成功！");
    }

    /**
     * 更新状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> setStatus(@PathVariable Integer status, Long[] ids) {
        LambdaUpdateWrapper<Setmeal> uw = new LambdaUpdateWrapper<>();
        uw.set(Setmeal::getStatus, status).in(Setmeal::getId, ids);

        setmealService.update(uw);

        return Result.success("状态更新成功！");
    }


    /**
     * 展示套餐列表
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public Result<List<Setmeal>> showList(Setmeal setmeal) {
        // 根据 categoryId 查询套餐
        LambdaQueryWrapper<Setmeal> qw = new LambdaQueryWrapper<>();
        qw.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId())
          .eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus())
          .orderByAsc(Setmeal::getPrice);
        List<Setmeal> setmeals = setmealService.list(qw);

        return Result.success(setmeals);
    }
}

