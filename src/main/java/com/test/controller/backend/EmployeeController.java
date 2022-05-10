package com.test.controller.backend;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.common.Result;
import com.test.domain.backend.Employee;
import com.test.exception.CustomException;
import com.test.service.backend.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 登录验证
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("login--->{}", employee);
        // 将页面密码 md5 处理
        String pwd = employee.getPassword();
        pwd = DigestUtils.md5DigestAsHex(pwd.getBytes());

        // 根据页面提交的 username 进入数据库查询比对
        LambdaQueryWrapper<Employee> qw = new LambdaQueryWrapper<Employee>();
        qw.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(qw);

        // 如果没有结果返回登陆失败
        if(emp == null) {
            return Result.error("用户名或者密码错误！");
        }

        // 密码比对
        if(!emp.getPassword().equals(pwd)) {
            return Result.error("密码输入不正确，请重试！");
        }

        // 查看员工状态，如果禁用，返回禁止登录
        if(emp.getStatus() == 0) {
            return Result.error("您已被禁止登陆，请联系管理员！");
        }

        // 登陆成功，将员工 id 存储到 Sessionz 中
        request.getSession().setAttribute("employee", emp.getId());

        return Result.success(emp);
    }

    /**
     * 登出用户
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {
        // 清除登录信息
        request.getSession().removeAttribute("employee");
        return Result.success("您已成功退出！");
    }

    /**
     * 显示列表
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page> showList(int page, int pageSize, String name) {
        // 添加页码对象
        Page<Employee> pageInfo = new Page(page, pageSize);

        // 设置查询 name 条件
        LambdaQueryWrapper<Employee> qw = new LambdaQueryWrapper<>();
        qw.like(StringUtils.isNotBlank(name), Employee::getName, name);

        // 排序显示
        qw.orderByDesc(Employee::getUpdateTime);

        // 执行查询
        employeeService.page(pageInfo, qw);

        return Result.success(pageInfo);
    }

    /**
     * 添加员工
     * @param employee
     * @return
     */
    @PostMapping
    public Result<String> addEmployee(@RequestBody Employee employee) {
        // 初始密码123456，进行 md5 处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        boolean flag = employeeService.save(employee);

        return flag ? Result.success("新员工添加成功！") : Result.error("新员工添加失败!");
    }

    /**
     * 修改员工状态
     * @param employee
     * @return
     */
    @PutMapping
    public Result<String> setStatus(@RequestBody Employee employee) {
        boolean flag = employeeService.updateById(employee);

        return flag ? Result.success("修改状态成功！") : Result.error("修改状态失败!");
    }

    /**
     * 更新员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Employee> updateEmployee(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);

        // 空结果，抛出异常
        if(employee == null) {
            throw new CustomException("请勿进行非法操作！");
        }

        // 将数据回显到 web
        return Result.success(employee);
    }
}
