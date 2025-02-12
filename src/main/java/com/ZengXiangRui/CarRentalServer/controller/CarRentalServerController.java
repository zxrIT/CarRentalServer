package com.ZengXiangRui.CarRentalServer.controller;

import com.ZengXiangRui.CarRentalServer.service.CarDetailService;
import com.ZengXiangRui.CarRentalServer.service.CarProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/business")
@SuppressWarnings("all")
@CrossOrigin
public class CarRentalServerController {
    private final CarProductService carProductService;
    private final CarDetailService carDetailService;

    @Autowired
    public CarRentalServerController(CarProductService carProductService,
                                     CarDetailService carDetailService) {
        this.carProductService = carProductService;
        this.carDetailService = carDetailService;
    }

    @GetMapping("/car/all")
    private String findAllCarProduct() {
        return carProductService.findAllCarProduct();
    }

    @GetMapping("/car/{id}")
    private String findCarById(@PathVariable String id) {
        return carProductService.findCarProductByCarId(id);
    }

    @GetMapping("/deatil/{id}")
    private String findDetailById(@PathVariable String id) {
        return carDetailService.findCarDetailByCarId(id);
    }
}
