package com.test.dao.backend;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.domain.backend.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryDao extends BaseMapper<Category>{
}
