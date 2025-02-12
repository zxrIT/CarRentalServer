package com.ZengXiangRui.CarRentalServer.service;

import com.ZengXiangRui.CarRentalServer.entity.CarDetail;
import com.baomidou.mybatisplus.extension.service.IService;


public interface CarDetailService extends IService<CarDetail> {
    String findCarDetailByCarId(String carId);
}
