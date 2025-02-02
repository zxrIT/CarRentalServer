package com.ZengXiangRui.CarRentalServer.controller;

import com.ZengXiangRui.CarRentalServer.RequestParam.UserRequestParam;
import com.ZengXiangRui.CarRentalServer.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
@CrossOrigin
@SuppressWarnings("all")
public class CarRentalServerRoleController {
    private final LoginService loginService;

    @Autowired
    public CarRentalServerRoleController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public String login(@RequestBody UserRequestParam userInfo) {
        return loginService.login(userInfo);
    }
}
