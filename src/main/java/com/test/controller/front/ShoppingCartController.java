package com.test.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.test.common.BaseContext;
import com.test.common.Result;
import com.test.domain.front.ShoppingCart;
import com.test.service.front.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 展示购物车列表
     * @return
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> showList() {
        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        Long userId = BaseContext.getCurrentId();

        qw.eq(userId != null, ShoppingCart::getUserId, userId)
          .orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(qw);

        log.info(String.valueOf(shoppingCarts));

        return Result.success(shoppingCarts);
    }

    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public Result<ShoppingCart> addShoppingCart(@RequestBody ShoppingCart shoppingCart) {
        ShoppingCart cart = shoppingCartService.saveByEntity(shoppingCart);

        return Result.success(cart);
    }

    /**
     * 取消购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public Result<ShoppingCart> subShoppingCart(@RequestBody ShoppingCart shoppingCart) {
        ShoppingCart cart = shoppingCartService.updateByEntity(shoppingCart);

        return Result.success(cart);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public Result<String> cleanShoppingCart(){
        shoppingCartService.remove();

        return Result.success("清空购物车成功！");
    }
}
