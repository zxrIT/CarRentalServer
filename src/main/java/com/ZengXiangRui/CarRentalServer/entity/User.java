package com.ZengXiangRui.CarRentalServer.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User {
    private String id;
    private String username;
    private String phone;
    @TableField("roleId")
    private Integer roleId;
    @TableField("userIcon")
    private String userIcon;
    private Integer points;
    private String password;
    private String account;
}
