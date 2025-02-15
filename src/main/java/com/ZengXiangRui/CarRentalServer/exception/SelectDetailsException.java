package com.ZengXiangRui.CarRentalServer.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SelectDetailsException extends RuntimeException {
    public SelectDetailsException(String message) {
        super(message);
    }
}
