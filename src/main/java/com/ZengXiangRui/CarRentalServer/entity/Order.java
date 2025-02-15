package com.ZengXiangRui.CarRentalServer.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("userOrder")
public class Order {
    @TableField("orderNo")
    private String orderNo;

    private String type;
    @TableField("carType")
    private String carType;

    @TableField("startTime")
    private Date startTime;

    @TableField("endTime")
    private Date endTime;

    @TableField("amount")
    private Integer amount;

    @TableField("userId")
    private String userId;
    private Integer status;

}
