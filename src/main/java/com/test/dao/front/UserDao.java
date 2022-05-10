package com.test.dao.front;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.domain.front.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseMapper<User>{
}
