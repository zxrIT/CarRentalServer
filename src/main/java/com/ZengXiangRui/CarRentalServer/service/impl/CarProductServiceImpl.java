package com.ZengXiangRui.CarRentalServer.service.impl;

import com.ZengXiangRui.CarRentalServer.Response.BaseResponse;
import com.ZengXiangRui.CarRentalServer.Response.BaseResponseUtil;
import com.ZengXiangRui.CarRentalServer.annotation.LoggerAnnotation;
import com.ZengXiangRui.CarRentalServer.content.RedisContent;
import com.ZengXiangRui.CarRentalServer.entity.CarProduct;
import com.ZengXiangRui.CarRentalServer.entity.PendingReviewCarProduct;
import com.ZengXiangRui.CarRentalServer.exception.DeleteCarProductException;
import com.ZengXiangRui.CarRentalServer.exception.IncrementCarProductException;
import com.ZengXiangRui.CarRentalServer.exception.SelectCarProductException;
import com.ZengXiangRui.CarRentalServer.exception.UpdateCarProductException;
import com.ZengXiangRui.CarRentalServer.mapper.CarDetailMapper;
import com.ZengXiangRui.CarRentalServer.mapper.CarProductMapper;
import com.ZengXiangRui.CarRentalServer.mapper.PendingReviewCarProductMapper;
import com.ZengXiangRui.CarRentalServer.redis.RedisIdWorker;
import com.ZengXiangRui.CarRentalServer.redis.RedisParam;
import com.ZengXiangRui.CarRentalServer.request.CarProductPayload;
import com.ZengXiangRui.CarRentalServer.service.CarProductService;
import com.ZengXiangRui.CarRentalServer.utils.Encryption;
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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@SuppressWarnings("all")
public class CarProductServiceImpl extends ServiceImpl<CarProductMapper, CarProduct>
        implements CarProductService {
    private final String redisKeySuccess = "car:product:success";
    private final String redisKeyAll = "car:product:all";
    private final StringRedisTemplate stringRedisTemplate;
    private final CarProductMapper carProductMapper;
    private final RedisIdWorker redisIdWorker;
    private final PendingReviewCarProductMapper pendingReviewCarProductMapper;
    private final CarDetailMapper carDetailMapper;

    @Autowired
    public CarProductServiceImpl(CarProductMapper carProductMapper,
                                 StringRedisTemplate stringRedisTemplate,
                                 RedisIdWorker redisIdWorker,
                                 PendingReviewCarProductMapper pendingReviewCarProductMapper,
                                 CarDetailMapper carDetailMapper
    ) {
        this.carProductMapper = carProductMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisIdWorker = redisIdWorker;
        this.pendingReviewCarProductMapper = pendingReviewCarProductMapper;
        this.carDetailMapper = carDetailMapper;
    }

    @Override
    @Transactional
    @LoggerAnnotation(operation = "通过待审核商品", dataSource = "mysql中的pendingReviewCarProduct表")
    public String throughVehicleInspectionCarProduct(String carId) throws IncrementCarProductException {
        try {
            PendingReviewCarProduct pendingReviewCarProduct = pendingReviewCarProductMapper.selectOne(
                    new LambdaQueryWrapper<PendingReviewCarProduct>().eq(
                            PendingReviewCarProduct::getId, carId
                    ));
            carProductMapper.insert(pendingReviewCarProduct);
            pendingReviewCarProductMapper.deleteById(pendingReviewCarProduct.getId());
            stringRedisTemplate.delete(redisKeySuccess);
            stringRedisTemplate.delete(redisKeyAll);
            return JsonSerialization.toJson(new BaseResponse<String>(
                    BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "通过成功"
            ));
        } catch (Exception exception) {
            throw new IncrementCarProductException(exception.getMessage());
        }
    }

    @Override
    @Transactional
    @LoggerAnnotation(operation = "驳回待审核商品", dataSource = "mysql中的pendingReviewCarProduct表")
    public String dismissVehicleInspectionCarProduct(String carId) throws DeleteCarProductException {
        try {
            PendingReviewCarProduct pendingReviewCarProduct = pendingReviewCarProductMapper.selectOne(
                    new LambdaQueryWrapper<PendingReviewCarProduct>().eq(
                            PendingReviewCarProduct::getId, carId
                    ));
            pendingReviewCarProductMapper.deleteById(pendingReviewCarProduct.getId());
            return JsonSerialization.toJson(new BaseResponse<String>(
                    BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "驳回成功"
            ));
        } catch (Exception exception) {
            throw new DeleteCarProductException(exception.getMessage());
        }
    }

    @Override
    @LoggerAnnotation(operation = "查询全部待审核商品", dataSource = "mysql中的pendingReviewCarProduct表")
    public String findVehicleInspectionCarProduct() throws SelectCarProductException {
        try {
            List<PendingReviewCarProduct> pendingReviewCarProducts =
                    pendingReviewCarProductMapper.selectList(new LambdaQueryWrapper<PendingReviewCarProduct>());
            return JsonSerialization.toJson(new BaseResponse<List<PendingReviewCarProduct>>(
                    BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, pendingReviewCarProducts
            ));
        } catch (Exception exception) {
            throw new SelectCarProductException(exception.getMessage());
        }
    }

    @Override
    @Transactional
    @LoggerAnnotation(operation = "查询全部商品", dataSource = "redis缓存或mysql中的car表")
    public String findAllCarProduct() {
        try {
            List<CarProduct> carProducts;
            RedisParam<List<CarProduct>> redisParam = new RedisParam<>();
            String redisCaching = stringRedisTemplate.opsForValue().get(redisKeyAll);
            if (!IsEmpty.isJsonEmpty(redisCaching)) {
                log.info("redis Caching hit");
                carProducts = ((RedisParam<List<CarProduct>>) JsonUtil.jsonToObject(redisCaching, RedisParam.class))
                        .getValue();
                return JsonSerialization.toJson(new BaseResponse<List<CarProduct>>(
                        BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, carProducts
                ));
            }
            carProducts = carProductMapper.selectList(new LambdaQueryWrapper<CarProduct>());
            redisParam.setKey(redisIdWorker.nextId(redisKeySuccess));
            redisParam.setValue(carProducts);
            stringRedisTemplate.opsForValue().set(
                    redisKeyAll, JsonUtil.objectToJson(redisParam),
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

    @Override
    @Transactional
    @LoggerAnnotation(operation = "查询全部上架商品", dataSource = "redis缓存或mysql中的car表")
    public String findAllCarProductStatusSuccess() {
        try {
            List<CarProduct> carProducts;
            RedisParam<List<CarProduct>> redisParam = new RedisParam<>();
            String redisCaching = stringRedisTemplate.opsForValue().get(redisKeySuccess);
            if (!IsEmpty.isJsonEmpty(redisCaching)) {
                log.info("redis Caching hit");
                carProducts = ((RedisParam<List<CarProduct>>) JsonUtil.jsonToObject(redisCaching, RedisParam.class))
                        .getValue();
                return JsonSerialization.toJson(new BaseResponse<List<CarProduct>>(
                        BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, carProducts
                ));
            }
            carProducts = carProductMapper.selectList(new LambdaQueryWrapper<CarProduct>()
                    .eq(CarProduct::getStatus, true));
            redisParam.setKey(redisIdWorker.nextId(redisKeySuccess));
            redisParam.setValue(carProducts);
            stringRedisTemplate.opsForValue().set(
                    redisKeySuccess, JsonUtil.objectToJson(redisParam),
                    RedisContent.CACHE_TTL, TimeUnit.MINUTES
            );
            log.info("redis缓存未命中，从数据库中查询后以加入redis缓存中");
            return JsonSerialization.toJson(new BaseResponse<List<CarProduct>>(
                    BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, carProducts
            ));
        } catch (Exception exception) {
            log.error("搜索全部上架汽车商品时发生错误");
            throw new SelectCarProductException(exception.getMessage());
        }
    }

    @Override
    @Transactional
    @LoggerAnnotation(operation = "根据id获取单个车辆信息", dataSource = "redis缓存或mysql中的car表")
    public String findCarProductByCarId(String carId) throws SelectCarProductException {
        try {
            CarProduct carProduct;
            String redisCaching = stringRedisTemplate.opsForValue().get(redisKeySuccess + ":" + carId);
            if (!IsEmpty.isJsonEmpty(redisCaching)) {
                log.info("redis Caching hit");
                carProduct = JsonUtil.jsonToObject(redisCaching, CarProduct.class);
                return JsonSerialization.toJson(new BaseResponse<CarProduct>(
                        BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, carProduct
                ));
            }
            carProduct = carProductMapper.selectOne(new LambdaQueryWrapper<CarProduct>().eq(
                    CarProduct::getId, carId
            ));
            stringRedisTemplate.opsForValue().set(
                    redisKeySuccess + ":" + carProduct.getId(), JsonUtil.objectToJson(carProduct),
                    RedisContent.CACHE_TTL, TimeUnit.MINUTES
            );
            log.info("redis缓存未命中，从数据库中查询后以加入redis缓存中");
            return JsonSerialization.toJson(new BaseResponse<CarProduct>(
                    BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, carProduct
            ));
        } catch (Exception exception) {
            throw new SelectCarProductException(exception.getMessage());
        }
    }

    @Override
    @Transactional
    @LoggerAnnotation(operation = "更新车辆信息", dataSource = "mysql中的car表")
    public String updateCarProduct(CarProduct carProduct) throws UpdateCarProductException {
        try {
            carProductMapper.updateById(carProduct);
            stringRedisTemplate.delete(redisKeySuccess);
            stringRedisTemplate.delete(redisKeyAll);
            return JsonSerialization.toJson(new BaseResponse<String>(
                    BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "更新成功"
            ));
        } catch (Exception exception) {
            throw new UpdateCarProductException(exception.getMessage());
        }
    }

    @Override
    @LoggerAnnotation(operation = "删除车辆信息", dataSource = "mysql中的car表和carDetail表")
    public String deleteCarProduct(String carId) throws DeleteCarProductException {
        try {
            carProductMapper.deleteById(carId);
            carDetailMapper.deleteById(carId);
            stringRedisTemplate.delete(redisKeySuccess);
            stringRedisTemplate.delete(redisKeyAll);
            return JsonSerialization.toJson(new BaseResponse<String>(
                    BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "删除成功"
            ));
        } catch (Exception exception) {
            throw new DeleteCarProductException(exception.getMessage());
        }
    }

    @Override
    @Transactional
    @LoggerAnnotation(operation = "添加车辆信息", dataSource = "mysql中的car表")
    public String incrementCarProduct(CarProductPayload carProductParam) throws IncrementCarProductException {
        try {
            PendingReviewCarProduct pendingReviewCarProduct = new PendingReviewCarProduct();
            String carId = Encryption.encryptToMd5(carProductParam.getName() + System.currentTimeMillis());
            pendingReviewCarProduct.setId(carId);
            pendingReviewCarProduct.setName(carProductParam.getName());
            pendingReviewCarProduct.setStatus(carProductParam.getStatus());
            pendingReviewCarProduct.setDisplacement(carProductParam.getDisplacement());
            pendingReviewCarProduct.setSpecifications(carProductParam.getSpecifications());
            pendingReviewCarProduct.setEnergy(carProductParam.getEnergy());
            pendingReviewCarProduct.setVolume(carProductParam.getVolume());
            pendingReviewCarProduct.setActuation(carProductParam.getActuation());
            pendingReviewCarProduct.setSeats(carProductParam.getSeats());
            pendingReviewCarProduct.setBrand(carProductParam.getBrand());
            pendingReviewCarProduct.setFirstTag(carProductParam.getFirstTag());
            pendingReviewCarProduct.setSecondTag(carProductParam.getSecondTag());
            pendingReviewCarProduct.setThirdTag(carProductParam.getThirdTag());
            pendingReviewCarProduct.setFourthTag(carProductParam.getFourthTag());
            pendingReviewCarProduct.setOriginalPrice(carProductParam.getOriginalPrice());
            pendingReviewCarProduct.setCurrentPrice(carProductParam.getCurrentPrice());
            pendingReviewCarProduct.setFuelOilNumber(carProductParam.getFuelOilNumber());
            if (carProductParam.getCarImage() != null && !carProductParam.getCarImage().isEmpty()) {
                MultipartFile file = carProductParam.getCarImage();
                String originalFilename = file.getOriginalFilename();
                String suffix = StringUtils.getFilenameExtension(originalFilename);
                String newFileName = carId + "." + suffix;
                System.out.println(newFileName);
                String projectPath = System.getProperty("user.dir");
                String savePath = projectPath + "/src/main/resources/static/";
                File saveDir = new File(savePath);
                if (!saveDir.exists()) {
                    saveDir.mkdirs();
                }
                File destFile = new File(savePath + newFileName);
                file.transferTo(destFile);
                pendingReviewCarProduct.setCarImage("/static/" + newFileName);
                pendingReviewCarProduct.setCarImage(newFileName);
            }
            pendingReviewCarProductMapper.insert(pendingReviewCarProduct);
            stringRedisTemplate.delete(redisKeySuccess);
            stringRedisTemplate.delete(redisKeyAll);
            return JsonSerialization.toJson(new BaseResponse<String>(
                    BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "添加成功"
            ));
        } catch (Exception exception) {
            throw new IncrementCarProductException(exception.getMessage());
        }
    }
}
