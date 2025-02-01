package com.ZengXiangRui.CarRentalServer.exception;

public class LoginException extends RuntimeException {
    public LoginException(String message) {
        super(message);
    }

    public LoginException() {
        super();
    }
}
