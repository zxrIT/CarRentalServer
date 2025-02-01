package com.ZengXiangRui.CarRentalServer.controller;

import com.ZengXiangRui.CarRentalServer.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
@SuppressWarnings("all")
public class CarRentalServerRoleController {
    private final LoginService loginService;

    @Autowired
    public CarRentalServerRoleController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login/{username}/{avatarUrl}")
    public String login(@PathVariable String username, @PathVariable String avatarUrl) {
        return loginService.login(username, avatarUrl);
    }
}
