package com.test.common;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class Result<T> implements Serializable {

    private Integer code;   // 编码：1成功，0或其他失败

    private String msg;

    private T data;

    private Map map = new HashMap();    //动态数据

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<T>();
        r.data = data;
        r.code = 1;
        return r;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> r = new Result<T>();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public Result<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}
