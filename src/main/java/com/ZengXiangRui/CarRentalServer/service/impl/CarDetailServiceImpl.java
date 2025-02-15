package com.ZengXiangRui.CarRentalServer.service.impl;

import com.ZengXiangRui.CarRentalServer.Response.BaseResponse;
import com.ZengXiangRui.CarRentalServer.Response.BaseResponseUtil;
import com.ZengXiangRui.CarRentalServer.annotation.LoggerAnnotation;
import com.ZengXiangRui.CarRentalServer.content.RedisContent;
import com.ZengXiangRui.CarRentalServer.entity.CarDetail;
import com.ZengXiangRui.CarRentalServer.exception.*;
import com.ZengXiangRui.CarRentalServer.mapper.CarDetailMapper;
import com.ZengXiangRui.CarRentalServer.service.CarDetailService;
import com.ZengXiangRui.CarRentalServer.utils.IsEmpty;
import com.ZengXiangRui.CarRentalServer.utils.JsonSerialization;
import com.ZengXiangRui.CarRentalServer.utils.JsonUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@SuppressWarnings("all")
@Slf4j
public class CarDetailServiceImpl extends ServiceImpl<CarDetailMapper, CarDetail>
        implements CarDetailService {
    private final String redisKey = "car:detail";
    private final CarDetailMapper carDetailMapper;
    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public CarDetailServiceImpl(CarDetailMapper carDetailMapper,
                                StringRedisTemplate stringRedisTemplate) {
        this.carDetailMapper = carDetailMapper;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    @LoggerAnnotation(operation = "修改车辆详情", dataSource = "mysql中的carDetail表")
    public String updateCarDetail(CarDetail carDetail) throws UpdateCarDetailException {
        try {
            carDetailMapper.updateById(carDetail);
            return JsonSerialization.toJson(new BaseResponse<String>(
                    BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "修改成功"
            ));
        } catch (Exception exception) {
            throw new UpdateCarDetailException(exception.getMessage());
        }
    }

    @Override
    @LoggerAnnotation(operation = "新增车辆详情", dataSource = "mysql中的carDetail表")
    public String incrementCarDetail(CarDetail carDetail) throws IncrementCarDetailException {
        try {
            boolean exists = carDetailMapper.exists(new LambdaQueryWrapper<CarDetail>()
                    .eq(CarDetail::getId, carDetail.getId()));
            if (exists) {
                carDetailMapper.updateById(carDetail);
                return JsonSerialization.toJson(new BaseResponse<String>(
                        BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "修改成功"
                ));
            } else {
                carDetailMapper.insert(carDetail);
                return JsonSerialization.toJson(new BaseResponse<String>(
                        BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "新增成功"
                ));
            }
        } catch (Exception exception) {
            throw new IncrementCarDetailException(exception.getMessage());
        }
    }

    @Override
    @LoggerAnnotation(operation = "查询全部车辆详情", dataSource = "mysql中的carDetail表")
    public String findCarDetails() throws SelectDetailsException {
        try {
            List<CarDetail> carDetails = carDetailMapper.selectList(new LambdaQueryWrapper<CarDetail>());
            return JsonSerialization.toJson(new BaseResponse<List<CarDetail>>(
                    BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, carDetails
            ));
        } catch (Exception exception) {
            throw new SelectDetailsException(exception.getMessage());
        }
    }

    @Override
    @Transactional
    @LoggerAnnotation(operation = "根据ID查询车辆详情", dataSource = "redis缓存或mysql中的carDetail表")
    public String findCarDetailByCarId(String carId) throws SelectCarDetailException {
        try {
            CarDetail carDetail;
            String redisCaching = stringRedisTemplate.opsForValue().get(redisKey + ":" + carId);
            if (!IsEmpty.isJsonEmpty(redisCaching)) {
                log.info("redis Caching hit");
                carDetail = JsonUtil.jsonToObject(redisCaching, CarDetail.class);
                return JsonSerialization.toJson(new BaseResponse<CarDetail>(
                        BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, carDetail
                ));
            }
            carDetail = carDetailMapper.selectOne(new LambdaQueryWrapper<CarDetail>().eq(CarDetail::getId, carId));
            stringRedisTemplate.opsForValue().set(redisKey + ":" + carId, JsonSerialization.toJson(carDetail)
                    , RedisContent.CACHE_TTL, TimeUnit.MINUTES);
            return JsonSerialization.toJson(new BaseResponse<CarDetail>(
                    BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, carDetail
            ));
        } catch (Exception exception) {
            throw new SelectCarDetailException(exception.getMessage());
        }
    }
}
