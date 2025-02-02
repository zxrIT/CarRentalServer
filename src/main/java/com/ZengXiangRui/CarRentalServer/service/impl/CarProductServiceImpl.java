package com.ZengXiangRui.CarRentalServer.service.impl;

import com.ZengXiangRui.CarRentalServer.Response.BaseResponse;
import com.ZengXiangRui.CarRentalServer.Response.BaseResponseUtil;
import com.ZengXiangRui.CarRentalServer.annotation.LoggerAnnotation;
import com.ZengXiangRui.CarRentalServer.content.RedisContent;
import com.ZengXiangRui.CarRentalServer.entity.CarProduct;
import com.ZengXiangRui.CarRentalServer.exception.SelectCarProductException;
import com.ZengXiangRui.CarRentalServer.mapper.CarProductMapper;
import com.ZengXiangRui.CarRentalServer.service.CarProductService;
import com.ZengXiangRui.CarRentalServer.utils.IsEmpty;
import com.ZengXiangRui.CarRentalServer.utils.JsonSerialization;
import com.ZengXiangRui.CarRentalServer.utils.JsonUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@SuppressWarnings("all")
public class CarProductServiceImpl extends ServiceImpl<CarProductMapper, CarProduct>
        implements CarProductService {
    private final String redisKey = "car:car_product";
    private final StringRedisTemplate stringRedisTemplate;
    private final CarProductMapper carProductMapper;

    @Autowired
    public CarProductServiceImpl(CarProductMapper carProductMapper,
                                 StringRedisTemplate stringRedisTemplate) {
        this.carProductMapper = carProductMapper;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    @LoggerAnnotation(operation = "查询全部商品", dataSource = "redis缓存或mysql中的car表")
    public String findAllCarProduct() {
        try {
            List<CarProduct> carProducts;
            String redisCaching = stringRedisTemplate.opsForValue().get(redisKey);
            if (!IsEmpty.isJsonEmpty(redisCaching)) {
                log.info("redis Caching hit");
                carProducts = (List<CarProduct>) JsonUtil.jsonToObject(redisCaching, List.class);
                return JsonSerialization.toJson(new BaseResponse<List<CarProduct>>(
                        BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, carProducts
                ));
            }
            carProducts = carProductMapper.selectList(new QueryWrapper<CarProduct>());
            stringRedisTemplate.opsForValue().set(
                    redisKey, JsonUtil.objectToJson(carProducts),
                    RedisContent.CACHE_TTL, TimeUnit.MINUTES
            );
            log.info("redis缓存未命中，从数据库中查询后以加入redis缓存中");
            return JsonSerialization.toJson(new BaseResponse<List<CarProduct>>(
                    BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, carProducts
            ));
        } catch (Exception exception) {
            log.error("搜索全部汽车商品时发生错误");
            throw new SelectCarProductException(exception.getMessage());
        }
    }
}
