package com.ZengXiangRui.CarRentalServer.service.impl;

import com.ZengXiangRui.CarRentalServer.RequestParam.UserRequestAdminLoginParam;
import com.ZengXiangRui.CarRentalServer.RequestParam.UserRequestParam;
import com.ZengXiangRui.CarRentalServer.Response.BaseResponse;
import com.ZengXiangRui.CarRentalServer.Response.BaseResponseUtil;
import com.ZengXiangRui.CarRentalServer.annotation.LoggerAnnotation;
import com.ZengXiangRui.CarRentalServer.entity.User;
import com.ZengXiangRui.CarRentalServer.exception.DeleteUserException;
import com.ZengXiangRui.CarRentalServer.exception.LoginException;
import com.ZengXiangRui.CarRentalServer.exception.SelectUserException;
import com.ZengXiangRui.CarRentalServer.exception.UpdateUserException;
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

import java.util.List;

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
    @LoggerAnnotation(operation = "查询全部用户", dataSource = "user")
    public String findAllUser() throws SelectUserException {
        try {
            List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>());
            return JsonSerialization.toJson(new BaseResponse<List<User>>(
                    BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, users
            ));
        } catch (Exception exception) {
            throw new SelectUserException(exception.getMessage());
        }
    }

    @Override
    public String loginAdmin(UserRequestAdminLoginParam userRequestAdminLoginParam) throws LoginException {
        try {
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .eq(User::getAccount, userRequestAdminLoginParam.getUsername()));
            if (user == null) {
                return JsonSerialization.toJson(new BaseResponse<String>(
                        BaseResponseUtil.FORBIDDEN_CODE, BaseResponseUtil.FORBIDDEN_MESSAGE, "暂无此账号请联系管理员为您开通"
                ));
            }
            if (user.getPassword().equals(Encryption.encryptToMd5(userRequestAdminLoginParam.getPassword()))) {
                return JsonSerialization.toJson(new BaseResponse<User>(
                        BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, user
                ));
            }
            return JsonSerialization.toJson(new BaseResponse<String>(
                    BaseResponseUtil.FORBIDDEN_CODE, BaseResponseUtil.FORBIDDEN_MESSAGE, "账号密码不正确请重试"
            ));
        } catch (Exception exception) {
            throw new LoginException(exception.getMessage());
        }
    }

    @Override
    public String updateUser(User user) throws UpdateUserException {
        try {
            userMapper.updateById(user);
            return JsonSerialization.toJson(new BaseResponse<String>(
                    BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "修改成功"
            ));
        } catch (Exception exception) {
            throw new UpdateUserException(exception.getMessage());
        }
    }

    @Override
    public String deleteUser(String id) throws DeleteUserException {
        try {
            userMapper.deleteById(id);
            return JsonSerialization.toJson(new BaseResponse<String>(
                    BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "删除成功"
            ));
        } catch (Exception exception) {
            throw new DeleteUserException(exception.getMessage());
        }
    }

    @Override
    @Transactional
    @LoggerAnnotation(operation = "用户登录", dataSource = "user")
    public String login(UserRequestParam userInfo) throws LoginException {
        try {
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>().
                    eq(User::getId, Encryption.encryptToMd5(userInfo.username)).
                    eq(User::getUsername, userInfo.username));
            if (user != null) {
                return JsonSerialization.toJson(new BaseResponse<User>(
                        BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, user
                ));
            }
            User newUser = new User();
            newUser.setUsername(userInfo.username);
            newUser.setId(Encryption.encryptToMd5(userInfo.username));
            newUser.setUserIcon(userInfo.avatarUrl);
            userMapper.insert(newUser);
            return JsonSerialization.toJson(new BaseResponse<User>(
                    BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, newUser
            ));
        } catch (Exception exception) {
            throw new LoginException(exception.getMessage());
        }
    }
}
