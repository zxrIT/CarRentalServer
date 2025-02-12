package com.ZengXiangRui.CarRentalServer.service;

import com.ZengXiangRui.CarRentalServer.entity.CarProduct;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CarProductService extends IService<CarProduct> {
    String findAllCarProduct();
    String findCarProductByCarId(String carId);
}
