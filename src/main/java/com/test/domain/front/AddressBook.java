package com.test.domain.front;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 地址簿
 */
@Data
public class AddressBook implements Serializable{
    private static final Long serialVersionUID = 1L;

    private Long id;

    private Long userId;                // 用户 id

    private String consignee;           // 收货人

    private String phone;

    private String sex;

    private String provinceCode;        // 省级区划编号

    private String provinceName;

    private String cityCode;            // 市级区划编号

    private String cityName;

    private String districtCode;        // 区级区划编号

    private String districtName;

    private String detail;              // 详细地址

    private String label;               // 标签

    private Integer isDefault;          //是否默认 0 否 1是

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    //创建人
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    //修改人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


    //是否删除
    private Integer isDeleted;

}
