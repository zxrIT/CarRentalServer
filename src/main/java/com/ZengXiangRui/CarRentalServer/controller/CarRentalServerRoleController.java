package com.ZengXiangRui.CarRentalServer.controller;

import com.ZengXiangRui.CarRentalServer.RequestParam.UserRequestAdminLoginParam;
import com.ZengXiangRui.CarRentalServer.RequestParam.UserRequestParam;
import com.ZengXiangRui.CarRentalServer.entity.User;
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

    @GetMapping("/get/all/user")
    private String getAllUser() {
        return loginService.findAllUser();
    }

    @PostMapping("/update/user")
    private String updateUser(@RequestBody User user) {
        return loginService.updateUser(user);
    }

    @DeleteMapping("/delete/user/{id}")
    private String deleteUser(@PathVariable String id) {
        return loginService.deleteUser(id);
    }

    @PostMapping("/login/admin")
    private String loginAdmin(@RequestBody UserRequestAdminLoginParam userRequestAdminLoginParam) {
        return loginService.loginAdmin(userRequestAdminLoginParam);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserRequestParam userInfo) {
        return loginService.login(userInfo);
    }
}
