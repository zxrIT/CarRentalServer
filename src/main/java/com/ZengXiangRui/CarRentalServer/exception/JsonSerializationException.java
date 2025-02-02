package com.ZengXiangRui.CarRentalServer.exception;

public class JsonSerializationException extends RuntimeException {
    public JsonSerializationException(String message) {
        super(message);
    }
    public JsonSerializationException() {
        super();
    }
}
