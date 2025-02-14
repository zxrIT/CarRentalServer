package com.ZengXiangRui.CarRentalServer.RequestParam;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CarProductParam {
    private String name;
    private String displacement;
    private String specifications;
    private String energy;
    private Integer volume;
    private String actuation;
    private Boolean status;
    private Integer seats;
    private String brand;
    private String firstTag;
    private String secondTag;
    private String thirdTag;
    private String fourthTag;
    private Integer originalPrice;
    private Integer currentPrice;
    private MultipartFile carImage;
    private String fuelOilNumber;
}
