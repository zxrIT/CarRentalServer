package com.ZengXiangRui.CarRentalServer.service;

import com.ZengXiangRui.CarRentalServer.RequestParam.CarProductParam;
import com.ZengXiangRui.CarRentalServer.entity.CarProduct;
import com.ZengXiangRui.CarRentalServer.request.CarProductPayload;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CarProductService extends IService<CarProduct> {
    String findAllCarProduct();

    String findAllCarProductStatusSuccess();

    String findCarProductByCarId(String carId);

    String updateCarProduct(CarProduct carProduct);

    String deleteCarProduct(String carId);

    String incrementCarProduct(CarProductPayload carProductPayload);
}
