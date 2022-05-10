package com.test.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.test.common.BaseContext;
import com.test.common.Result;
import com.test.domain.front.User;
import com.test.service.front.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    // TODO: 接收 code 需要修改 user 为 map
    public Result<User> login(@RequestBody User user, HttpSession session) {
        String phone = user.getPhone();

        // TODO: 判断验证码是否正确

        // 查询手机号
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        qw.eq(User::getPhone, phone);
        User u = userService.getOne(qw);

        // 判断是否存在
        if(u == null) {
            // 不存在则注册用户
            u = new User();
            u.setPhone(phone);
            u.setStatus(1);
            userService.save(u);
        }

        session.setAttribute("user", u.getId());

        return Result.success(u);
    }

    /**
     * 退出登录
     * @return
     */
    @PostMapping("/loginout")
    public Result<String> logout(HttpSession session) {
        session.removeAttribute("user");

        return Result.success("退出成功！");
    }

}
