package com.ZengXiangRui.CarRentalServer.exception;

public class SelectUserException extends RuntimeException {
    public SelectUserException(String message) {
        super(message);
    }
    public SelectUserException() {
        super();
    }
}
