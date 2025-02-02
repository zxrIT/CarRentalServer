package com.ZengXiangRui.CarRentalServer.controller;

import com.ZengXiangRui.CarRentalServer.service.CarProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/business")
@SuppressWarnings("all")
@CrossOrigin
public class CarRentalServerController {
    private final CarProductService carProductService;

    @Autowired
    public CarRentalServerController(CarProductService carProductService) {
        this.carProductService = carProductService;
    }

    @GetMapping("/car/all")
    private String findAllCarProduct() {
        return carProductService.findAllCarProduct();
    }
}
