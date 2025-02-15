package com.ZengXiangRui.CarRentalServer.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UpdateUserException extends RuntimeException {
    public UpdateUserException(String message) {
        super(message);
    }
}
