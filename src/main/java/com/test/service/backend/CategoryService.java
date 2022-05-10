package com.test.service.backend;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.domain.backend.Category;

public interface CategoryService extends IService<Category> {
    /**
     * 根据 id 删除分类，删除之前进行判断
     * @return
     */
    public void remove(Long id);
}
