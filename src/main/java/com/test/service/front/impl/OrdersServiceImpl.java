package com.test.service.front.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.common.BaseContext;
import com.test.dao.front.OrdersDao;
import com.test.domain.front.*;
import com.test.dto.OrdersDto;
import com.test.exception.CustomException;
import com.test.service.backend.DishFlavorService;
import com.test.service.backend.DishService;
import com.test.service.front.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersDao, Orders> implements OrdersService {

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveByEntity(Orders orders) {
        Long userId = BaseContext.getCurrentId();
        User user = userService.getById(userId);

        // 判断订单地址是否可以下单
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        if(addressBook == null) {
            throw new CustomException("地址有误，不能下单！");
        }

        // 取出购物车
        LambdaQueryWrapper<ShoppingCart> scqw = new LambdaQueryWrapper<>();
        scqw.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(scqw);
        if(shoppingCartList == null) {
            throw new CustomException("购物车为空，不能下单！");
        }

        // 订单号
        long orderId = IdWorker.getId();

        // 验算金额
        AtomicInteger amount = new AtomicInteger(0);

        // 保存订单商品明细
        List<OrderDetail> orderDetails = shoppingCartList.stream().map(item -> {
            // 验算金额
            amount.getAndAdd(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());

            // 每个商品一个订单明细
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            return orderDetail;
        }).collect(Collectors.toList());

        // 保存订单
        orders.setId(orderId);
        orders.setUserId(userId);
        orders.setStatus(2);    // 等待派送
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setNumber(String.valueOf(orderId)); //订单号
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
               + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
               + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
               + (addressBook.getDetail() == null ? "" : addressBook.getDetail())
        );

        // 向订单表插数据
        this.save(orders);

        // 向订单明细表插入多条数据
        orderDetailService.saveBatch(orderDetails);

        // 清空购物车
        shoppingCartService.remove();

    }
}
