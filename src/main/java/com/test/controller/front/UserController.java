package com.test.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.test.common.BaseContext;
import com.test.common.Result;
import com.test.domain.front.User;
import com.test.service.front.UserService;
import com.test.utils.SMSUtils;
import com.test.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发送手机验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user) {
        String phone = user.getPhone();
        if(StringUtils.isNotBlank(phone)) {
            // 生成验证码
            String code = ValidateCodeUtils.generateValidateCode(6).toString();
            log.info("code------->{}", code);

            // 验证码存入 redis
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);

            // 发送验证码


            return Result.success("短信发送成功！");
        }

        return Result.error("短信发送失败!");
    }


    @PostMapping("/login")
    public Result<User> login(@RequestBody Map map, HttpSession session) {
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();

        // 获取 redis 中的验证码
        String codeSession = (String) redisTemplate.opsForValue().get(phone);

        // 进行验证码比对
        if(codeSession.equals(code)) {
            // 查询手机号
            LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
            qw.eq(User::getPhone, phone);
            User u = userService.getOne(qw);

            // 判断是否存在
            if (u == null) {
                // 不存在则注册用户
                u = new User();
                u.setPhone(phone);
                u.setStatus(1);
                userService.save(u);
            }

            session.setAttribute("user", u.getId());

            // 用户登陆成功，删除 redis 中的缓存
            redisTemplate.delete(phone);

            return Result.success(u);
        }

        return Result.error("登录失败！");
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
