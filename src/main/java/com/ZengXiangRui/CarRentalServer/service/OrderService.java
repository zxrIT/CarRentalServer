package com.ZengXiangRui.CarRentalServer.service;

import com.ZengXiangRui.CarRentalServer.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrderService extends IService<Order> {
    String findAllOrder();

    String findUserOrder(String id);

    String insertOrder(Order order);
}
