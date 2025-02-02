package com.ZengXiangRui.CarRentalServer.handler;

import com.ZengXiangRui.CarRentalServer.Response.BaseResponse;
import com.ZengXiangRui.CarRentalServer.Response.BaseResponseUtil;
import com.ZengXiangRui.CarRentalServer.exception.LoginException;
import com.ZengXiangRui.CarRentalServer.utils.JsonSerialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public String exceptionHandler(Exception exception) {
        if (exception instanceof LoginException) {
            return JsonSerialization.toJson(new BaseResponse<>(
                    BaseResponseUtil.SERVER_ERROR_CODE, BaseResponseUtil.SERVER_ERROR_MESSAGE,
                    "登录失败，请稍后重试"
            ));
        }
        logger.error("==============log start =============");
        logger.error("exception type: {}", exception.getClass().getName());
        logger.error("exception message: {}", exception.getMessage());
        logger.error("==============log end =============");
        return JsonSerialization.toJson(new BaseResponse<>(
                BaseResponseUtil.SERVER_ERROR_CODE, BaseResponseUtil.SERVER_ERROR_MESSAGE,
                "服务器去了火星！！！请稍后重试"
        ));
    }
}
