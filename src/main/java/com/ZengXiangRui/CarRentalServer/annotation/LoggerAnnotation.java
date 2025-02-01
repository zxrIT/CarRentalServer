package com.ZengXiangRui.CarRentalServer.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoggerAnnotation {
    String operation() default "";
    String dataSource() default "";
}
