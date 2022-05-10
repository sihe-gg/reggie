package com.test.dao.front;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.domain.front.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookDao extends BaseMapper<AddressBook> {
}
