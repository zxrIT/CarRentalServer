package com.ZengXiangRui.CarRentalServer.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("car")
public class CarProduct {
    private String id;
    private String name;
    private String displacement;
    private String specifications;
    private String energy;
    private Integer volume;
    private String actuation;
    private Boolean status;
    private Integer seats;
    private String brand;

    @TableField("firstTag")
    private String firstTag;

    @TableField("secondTag")
    private String secondTag;

    @TableField("thirdTag")
    private String thirdTag;

    @TableField("fourthTag")
    private String fourthTag;

    @TableField("originalPrice")
    private Integer originalPrice;

    @TableField("currentPrice")
    private Integer currentPrice;

    @TableField("carImage")
    private String carImage;

    @TableField("fuelOilNumber")
    private String fuelOilNumber;
}
