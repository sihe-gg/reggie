package com.test.domain.front;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息
 */
@Data
public class User implements Serializable {

    private static final Long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String phone;

    private String sex;         //  0 女 1 男

    private String idNumber;    // 身份证号码

    private String avatar;      // 头像

    private Integer status;
}
