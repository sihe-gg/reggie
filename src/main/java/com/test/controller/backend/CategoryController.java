package com.test.controller.backend;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.common.Result;
import com.test.domain.backend.Category;
import com.test.service.backend.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 展示列表
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public Result<Page> showList(int page, int pageSize) {
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> qw = new LambdaQueryWrapper<>();

        // 添加排序条件
        qw.orderByAsc(Category::getSort);

        // 执行查询操作
        categoryService.page(pageInfo, qw);

        return Result.success(pageInfo);
    }

    /**
     * 新增菜品
     * @param category
     * @return
     */
    @PostMapping
    public Result<String> addCategory(@RequestBody Category category) {
        boolean flag= categoryService.save(category);
        return flag ? Result.success("添加菜品成功！") : Result.error("添加菜品失败!");
    }

    @PutMapping
    public Result<String> updateCategory(@RequestBody Category category) {
        boolean flag = categoryService.updateById(category);
        return flag ? Result.success("修改菜品成功！") : Result.error("修改菜品失败!");
    }

    @DeleteMapping
    public Result<String> deleteCategory(Long ids) {
        categoryService.remove(ids);
        return Result.success("删除菜品成功！");
    }

    /**
     * 菜品分类选择框展示
     * @param category
     * @return
     */
    @GetMapping("/list")
    public Result<List<Category>> selectAll(Category category) {
        LambdaQueryWrapper<Category> qw = new LambdaQueryWrapper<>();
        qw.eq(category.getType() != null, Category::getType, category.getType());
        List<Category> list = categoryService.list(qw);

        return Result.success(list);
    }
}
