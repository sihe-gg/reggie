package com.test.service.front;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.domain.front.ShoppingCart;

public interface ShoppingCartService extends IService<ShoppingCart> {

    /**
     * 添加购物车商品
     * @param shoppingCart
     * @return
     */
    public ShoppingCart saveByEntity(ShoppingCart shoppingCart);

    /**
     * 减除购物车商品
     * @param shoppingCart
     * @return
     */
    public ShoppingCart updateByEntity(ShoppingCart shoppingCart);

    /**
     * 清空购物车
     */
    public boolean remove();
}
