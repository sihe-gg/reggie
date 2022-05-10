package com.test.common;

import com.test.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */
@Slf4j
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> duplicationException(SQLIntegrityConstraintViolationException ex) {
        // 判断是否重复关键字
        String message = ex.getMessage();
        if(message.contains("Duplicate entry")) {
            String[] split = message.split(" ");
            message = split[2] + " 已存在！";
            return Result.error(message);
        }

        return Result.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)
    public Result<String> customException(CustomException ex) {
        log.info(ex.getMessage());
        return Result.error(ex.getMessage());
    }

}
