package com.ZengXiangRui.CarRentalServer.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("CarDetail")
public class CarDetail {
    private String id;
    @TableField("autoAirConditioner")
    private String autoAirConditioner;

    @TableField("airSystem")
    private String airSystem;

    private boolean radar;

    private String chair;

    @TableField("tirePressureMonitoringFunction")
    private String tirePressureMonitoringFunction;

    @TableField("bluetoothUSB")
    private String bluetoothUSB;

    @TableField("nearAndFarLightType")
    private String nearAndFarLightType;

    @TableField("automaticParking")
    private boolean automaticParking;

    @TableField("launchWithOneClick")
    private boolean launchWithOneClick;

    @TableField("driverAssistanceImage")
    private boolean driverAssistanceImage;

    @TableField("carPlayer")
    private boolean carPlayer;

    @TableField("skylight")
    private boolean skylight;

}
