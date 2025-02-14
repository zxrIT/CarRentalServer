package com.ZengXiangRui.CarRentalServer.exception;

public class DeleteCarProductException extends RuntimeException {
    public DeleteCarProductException(String message) {
        super(message);
    }
    public DeleteCarProductException() {
        super();
    }
}
