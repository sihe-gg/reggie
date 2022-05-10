package com.test.service.front.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.common.BaseContext;
import com.test.dao.front.ShoppingCartDao;
import com.test.domain.front.ShoppingCart;
import com.test.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartDao, ShoppingCart> implements com.test.service.front.ShoppingCartService{

    @Transactional
    @Override
    public ShoppingCart saveByEntity(ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        Long userId = BaseContext.getCurrentId();

        // 查询商品是否在购车中，查询条件为 菜品 或 套餐
        qw.eq(ShoppingCart::getUserId, userId)
                .eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId())
                .eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());

        ShoppingCart shoppingCartOne = this.getOne(qw);

        shoppingCart.setUserId(userId);
        if(shoppingCartOne == null) {
            // 不存在则添加购物车
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            this.save(shoppingCart);
            shoppingCartOne = shoppingCart;
        }else {
            // 存在购物车则数量 +1
            shoppingCartOne.setNumber(shoppingCartOne.getNumber() + 1);
            this.updateById(shoppingCartOne);
        }

        return shoppingCartOne;
    }

    @Transactional
    @Override
    public ShoppingCart updateByEntity(ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        qw.eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId())
                .eq(shoppingCart.getSetmealId()!= null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());

        ShoppingCart shoppingCartOne = this.getOne(qw);
        if(shoppingCartOne == null) {
            throw new CustomException("无效操作，请刷新再试！");
        }

        // 减少购物车数量
        shoppingCartOne.setNumber(shoppingCartOne.getNumber() - 1);
        this.updateById(shoppingCartOne);

        // 返回数据
        shoppingCart = shoppingCartOne;

        if(shoppingCartOne.getNumber() <= 0) {
            this.removeById(shoppingCartOne);
        }

        return shoppingCart;
    }

    @Transactional
    @Override
    public boolean remove() {
        Long userId = BaseContext.getCurrentId();

        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        qw.eq(userId != null, ShoppingCart::getUserId, userId);

        // 清空
        return this.remove(qw);
    }
}
