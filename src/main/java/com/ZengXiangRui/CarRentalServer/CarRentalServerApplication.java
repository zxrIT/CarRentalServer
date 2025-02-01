package com.ZengXiangRui.CarRentalServer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@SuppressWarnings("all")
@MapperScan("com.ZengXiangRui.CarRentalServer.mapper")
public class CarRentalServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarRentalServerApplication.class, args);
    }
}
