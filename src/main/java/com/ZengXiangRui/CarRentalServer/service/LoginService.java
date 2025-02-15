package com.ZengXiangRui.CarRentalServer.service;

import com.ZengXiangRui.CarRentalServer.RequestParam.UserRequestAdminLoginParam;
import com.ZengXiangRui.CarRentalServer.RequestParam.UserRequestParam;
import com.ZengXiangRui.CarRentalServer.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface LoginService extends IService<User> {
    String findAllUser();

    String loginAdmin(UserRequestAdminLoginParam userRequestAdminLoginParam);

    String updateUser(User user);

    String deleteUser(String id);

    String login(UserRequestParam userInfo);
}
