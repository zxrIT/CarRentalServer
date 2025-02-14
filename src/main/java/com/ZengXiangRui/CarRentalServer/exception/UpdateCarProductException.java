package com.ZengXiangRui.CarRentalServer.exception;

public class UpdateCarProductException extends RuntimeException {
    public UpdateCarProductException(String message) {
        super(message);
    }
    public UpdateCarProductException() {
      super();
    }
}
