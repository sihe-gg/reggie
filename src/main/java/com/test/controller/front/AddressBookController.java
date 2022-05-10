package com.test.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.test.common.BaseContext;
import com.test.common.Result;
import com.test.domain.front.AddressBook;
import com.test.service.front.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 显示地址列表
     * @return
     */
    @GetMapping("/list")
    public Result<List<AddressBook>> showList() {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<AddressBook> qw = new LambdaQueryWrapper<>();

        qw.eq(userId != null, AddressBook::getUserId, userId);
        List<AddressBook> addressBookList = addressBookService.list(qw);


        return Result.success(addressBookList);
    }

    /**
     * 添加地址
     * @param addressBook
     * @return
     */
    @PostMapping
    public Result<String> addAddr(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressBookService.save(addressBook);

        return Result.success("添加地址成功！");
    }

    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    @Transactional
    @PutMapping("/default")
    public Result<String> setDefaultAddr(@RequestBody AddressBook addressBook) {
        // 默认地址只能有一个，先把所有地址设为 0，再设置此地址为 1
        Long userId = BaseContext.getCurrentId();
        LambdaUpdateWrapper<AddressBook> uw = new LambdaUpdateWrapper<>();

        uw.set(AddressBook::getIsDefault, 0)
          .eq(AddressBook::getUserId, userId);
        addressBookService.update(uw);

        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);

        return Result.success("设为默认成功！");
    }


    /**
     * 获取默认地址
     * @return
     */
    @GetMapping("/default")
    public Result<AddressBook> setDefaultAddr() {
        LambdaQueryWrapper<AddressBook> qw = new LambdaQueryWrapper<>();
        qw.eq(AddressBook::getUserId, BaseContext.getCurrentId())
          .eq(AddressBook::getIsDefault, 1);

        AddressBook addressBook = addressBookService.getOne(qw);

        return Result.success(addressBook);
    }
}
