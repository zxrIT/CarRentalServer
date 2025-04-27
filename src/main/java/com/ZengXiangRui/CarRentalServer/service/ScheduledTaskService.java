package com.ZengXiangRui.CarRentalServer.service;

import com.ZengXiangRui.CarRentalServer.entity.Order;
import com.ZengXiangRui.CarRentalServer.mapper.OrderMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@SuppressWarnings("all")
public class ScheduledTaskService {

    private final OrderMapper orderMapper;

    @Autowired
    public ScheduledTaskService(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Scheduled(cron = "0 0/5 * * * *")
    public void scheduledTask() {
        List<Order> orders = orderMapper.selectList(new LambdaQueryWrapper<Order>());
        LocalDateTime now = LocalDateTime.now();
        for (Order order : orders) {
            boolean flagArount = now.isAfter(new Timestamp(order.getStartTime().getTime()).toLocalDateTime()) &&
                    now.isBefore(new Timestamp(order.getEndTime().getTime()).toLocalDateTime());
            boolean flagEnd = now.isAfter(new Timestamp(order.getEndTime().getTime()).toLocalDateTime());
            boolean flagStart = now.isBefore(new Timestamp(order.getStartTime().getTime()).toLocalDateTime());
            if (flagArount) {
                order.setStatus(1);
                orderMapper.updateById(order);
            } else if (flagEnd) {
                order.setStatus(2);
                orderMapper.updateById(order);
            } else if (flagStart) {
                order.setStatus(0);
                orderMapper.updateById(order);
            }
        }
    }
}
