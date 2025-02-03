package com.ZengXiangRui.CarRentalServer.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisParam<T> {
    private Long key;
    private T value;
}
