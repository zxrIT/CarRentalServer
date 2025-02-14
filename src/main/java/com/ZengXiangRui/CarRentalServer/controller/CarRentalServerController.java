package com.ZengXiangRui.CarRentalServer.controller;

import com.ZengXiangRui.CarRentalServer.RequestParam.CarProductParam;
import com.ZengXiangRui.CarRentalServer.entity.CarProduct;
import com.ZengXiangRui.CarRentalServer.request.CarProductPayload;
import com.ZengXiangRui.CarRentalServer.service.CarDetailService;
import com.ZengXiangRui.CarRentalServer.service.CarProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/business")
//@SuppressWarnings("all")
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

    @GetMapping("/car")
    private String findAllCar() {
        return carProductService.findAllCarProduct();
    }

    @GetMapping("/car/all")
    private String findAllCarProduct() {
        return carProductService.findAllCarProductStatusSuccess();
    }

    @GetMapping("/car/{id}")
    private String findCarById(@PathVariable String id) {
        return carProductService.findCarProductByCarId(id);
    }

    @GetMapping("/deatil/{id}")
    private String findDetailById(@PathVariable String id) {
        return carDetailService.findCarDetailByCarId(id);
    }

    @PostMapping("/update/car")
    private String updateCarProduct(@RequestBody CarProduct carProduct) {
        return carProductService.updateCarProduct(carProduct);
    }

    @DeleteMapping("/delete/car/{id}")
    private String deleteCarProduct(@PathVariable String id) {
        return carProductService.deleteCarProduct(id);
    }

    @PostMapping("/increment/car/{name}/{displacement}/{specifications}" +
            "/{firstTag}/{secondTag}/{thirdTag}/{fourthTag}/{originalPrice}/{currentPrice}" +
            "/{fuelOilNumber}/{energy}/{volume}/{actuation}/{seats}/{status}/{brand}")
    public String incrementCarProduct(@PathVariable String name,
                                      @PathVariable String displacement,
                                      @PathVariable String specifications,
                                      @PathVariable String firstTag,
                                      @PathVariable String secondTag,
                                      @PathVariable String thirdTag,
                                      @PathVariable String fourthTag,
                                      @PathVariable Integer originalPrice,
                                      @PathVariable Integer currentPrice,
                                      @PathVariable Integer fuelOilNumber,
                                      @PathVariable String energy,
                                      @PathVariable Integer volume,
                                      @PathVariable String actuation,
                                      @PathVariable Integer seats,
                                      @PathVariable boolean status,
                                      @PathVariable String brand,
                                      @RequestBody MultipartFile file) {
        CarProductPayload carProductPayload = new CarProductPayload();
        carProductPayload.setName(name);
        carProductPayload.setDisplacement(displacement);
        carProductPayload.setSpecifications(specifications);
        carProductPayload.setFirstTag(firstTag);
        carProductPayload.setSecondTag(secondTag);
        carProductPayload.setThirdTag(thirdTag);
        carProductPayload.setFourthTag(fourthTag);
        carProductPayload.setOriginalPrice(originalPrice);
        carProductPayload.setCurrentPrice(currentPrice);
        carProductPayload.setCarImage(file);
        carProductPayload.setEnergy(energy);
        carProductPayload.setSeats(seats);
        carProductPayload.setStatus(status);
        carProductPayload.setBrand(brand);
        carProductPayload.setActuation(actuation);
        carProductPayload.setFuelOilNumber(fuelOilNumber);
        carProductPayload.setVolume(volume);
        return carProductService.incrementCarProduct(carProductPayload);
    }
}
