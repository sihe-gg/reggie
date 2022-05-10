package com.test.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.test.common.BaseContext;
import com.test.common.Result;
import com.test.domain.front.OrderDetail;
import com.test.domain.front.Orders;
import com.test.domain.front.User;
import com.test.dto.OrdersDto;
import com.test.service.front.AddressBookService;
import com.test.service.front.OrderDetailService;
import com.test.service.front.OrdersService;
import com.test.service.front.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private UserService userService;

    /**
     * 提交订单
     * @return
     */
    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Orders order) {
        ordersService.saveByEntity(order);

        return Result.success("支付成功！");
    }

    /**
     * 显示订单列表
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public Result<Page<OrdersDto>> showList(int page, int pageSize) {
        Page<Orders> ordersPage = new PageDTO<>(page, pageSize);
        Page<OrdersDto> ordersDtoPage = new PageDTO<>();
        Long userId = BaseContext.getCurrentId();
        User user = userService.getById(userId);

        // 查询订单条件
        LambdaQueryWrapper<Orders> oqw = new LambdaQueryWrapper<>();
        oqw.eq(userId != null, Orders::getUserId, userId)
          .orderByDesc(Orders::getCheckoutTime);
        ordersService.page(ordersPage, oqw);

        // 赋值查询的页码
        BeanUtils.copyProperties(ordersPage, ordersDtoPage, "records");

        // 用户订单
        List<Orders> orders = ordersPage.getRecords();

        // 查询订单明细
        LambdaQueryWrapper<OrderDetail> odqw = new LambdaQueryWrapper<>();
        List<OrdersDto> ordersDtos = orders.stream().map(item -> {
            odqw.clear();
            odqw.eq(item.getId() != null, OrderDetail::getOrderId, item.getId());
            List<OrderDetail> orderDetails = orderDetailService.list(odqw);

            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            ordersDto.setOrderDetails(orderDetails);
            ordersDto.setPhone(item.getPhone());
            ordersDto.setUserName(user.getName());
            ordersDto.setConsignee(item.getConsignee());
            ordersDto.setAddress(item.getAddress());

            return ordersDto;
        }).collect(Collectors.toList());

        ordersDtoPage.setRecords(ordersDtos);

        return Result.success(ordersDtoPage);
    }

    /**
     * 查询订单列表
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public Result<Page> showList(Integer page, Integer pageSize, Integer number
            , String beginTime, String endTime) {

        Page<Orders> pageInfo = new Page<>();

        LambdaQueryWrapper<Orders> qw = new LambdaQueryWrapper<>();
        qw.like(number != null, Orders::getId, number)
          .between(beginTime != null && endTime != null, Orders::getOrderTime, beginTime, endTime)
          .orderByDesc(Orders::getOrderTime);

        ordersService.page(pageInfo, qw);

        return Result.success(pageInfo);
    }

    /**
     * 更新派送状态
     * @param orders
     * @return
     */
    @PutMapping
    public Result<String> setStatus(@RequestBody Orders orders) {
        ordersService.updateById(orders);

        return Result.success("修改成功!");
    }
}
