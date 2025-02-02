package com.ZengXiangRui.CarRentalServer.exception;

public class SelectCarProductException extends RuntimeException {
    public SelectCarProductException(String message) {
        super(message);
    }
    public SelectCarProductException() {
        super();
    }
}
