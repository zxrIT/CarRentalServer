package com.ZengXiangRui.CarRentalServer.service.impl;

import com.ZengXiangRui.CarRentalServer.Response.BaseResponse;
import com.ZengXiangRui.CarRentalServer.Response.BaseResponseUtil;
import com.ZengXiangRui.CarRentalServer.annotation.LoggerAnnotation;
import com.ZengXiangRui.CarRentalServer.entity.User;
import com.ZengXiangRui.CarRentalServer.exception.LoginException;
import com.ZengXiangRui.CarRentalServer.mapper.UserMapper;
import com.ZengXiangRui.CarRentalServer.service.LoginService;
import com.ZengXiangRui.CarRentalServer.utils.Encryption;
import com.ZengXiangRui.CarRentalServer.utils.JsonSerialization;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@SuppressWarnings("all")
@Slf4j
public class LoginServiceImpl extends ServiceImpl<UserMapper, User> implements LoginService {
    private final UserMapper userMapper;

    @Autowired
    public LoginServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    @LoggerAnnotation(operation = "用户登录", dataSource = "user")
    public String login(String username, String avatarUrl) throws LoginException {
        try {
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>().
                    eq(User::getId, Encryption.encryptToMd5(username)).
                    eq(User::getUsername, username));
            if (user != null) {
                return JsonSerialization.toJson(new BaseResponse<User>(
                        BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, user
                ));
            }
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setId(Encryption.encryptToMd5(username));
            newUser.setUserIcon(avatarUrl);
            userMapper.insert(newUser);
            return JsonSerialization.toJson(new BaseResponse<User>(
                    BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, newUser
            ));
        } catch (Exception exception) {
            throw new LoginException(exception.getMessage());
        }
    }
}
