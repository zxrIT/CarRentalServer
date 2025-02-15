package com.ZengXiangRui.CarRentalServer.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class IncrementCarDetailException extends RuntimeException {
    public IncrementCarDetailException(String message) {
        super(message);
    }
}
