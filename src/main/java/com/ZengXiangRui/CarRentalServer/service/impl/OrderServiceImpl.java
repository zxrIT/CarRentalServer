package com.ZengXiangRui.CarRentalServer.service.impl;

import com.ZengXiangRui.CarRentalServer.Response.BaseResponse;
import com.ZengXiangRui.CarRentalServer.Response.BaseResponseUtil;
import com.ZengXiangRui.CarRentalServer.annotation.LoggerAnnotation;
import com.ZengXiangRui.CarRentalServer.entity.Order;
import com.ZengXiangRui.CarRentalServer.entity.User;
import com.ZengXiangRui.CarRentalServer.exception.SelectOrderException;
import com.ZengXiangRui.CarRentalServer.mapper.OrderMapper;
import com.ZengXiangRui.CarRentalServer.mapper.UserMapper;
import com.ZengXiangRui.CarRentalServer.service.OrderService;
import com.ZengXiangRui.CarRentalServer.utils.JsonSerialization;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings("all")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
        implements OrderService {
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper, UserMapper userMapper) {
        this.orderMapper = orderMapper;
        this.userMapper = userMapper;
    }

    @Override
    @LoggerAnnotation(operation = "查询全部订单", dataSource = "mysql中的order表")
    public String findAllOrder() {
        try {
            List<Order> orders = orderMapper.selectList(new LambdaQueryWrapper<Order>());
            return JsonSerialization.toJson(new BaseResponse<List<Order>>(
                    BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, orders
            ));
        } catch (Exception exception) {
            throw new SelectOrderException(exception.getMessage());
        }
    }

    @Override
    @LoggerAnnotation(operation = "添加用户订单", dataSource = "mysql中的order表")
    public String insertOrder(Order order) {
        try {
            Order orderObject = orderMapper.selectOne(new QueryWrapper<Order>().eq("orderNo", order.getOrderNo()));
            if (orderObject == null) {
                orderMapper.insert(order);
                return JsonSerialization.toJson(new BaseResponse<String>(
                        BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "预定成功"
                ));
            }
            return JsonSerialization.toJson(new BaseResponse<String>(
                    BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "添加失败订单已存在"
            ));
        } catch (Exception exception) {
            throw new SelectOrderException(exception.getMessage());
        }
    }

    @Override
    @LoggerAnnotation(operation = "查询用户订单", dataSource = "mysql中的order表")
    public String findUserOrder(String userId) throws SelectOrderException {
        try {
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, userId));
            if (user == null) {
                return JsonSerialization.toJson(new BaseResponse<String>(
                        BaseResponseUtil.CLIENT_ERROR_CODE, BaseResponseUtil.CLIENT_ERROR_MESSAGE, "用户不存在"
                ));
            }
            System.out.println(user);
            List<Order> orders = orderMapper.selectList(new LambdaQueryWrapper<Order>()
                    .eq(Order::getUserId, user.getId()));
            return JsonSerialization.toJson(new BaseResponse<List<Order>>(
                    BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, orders
            ));
        } catch (Exception exception) {
            throw new SelectOrderException(exception.getMessage());
        }
    }
}
