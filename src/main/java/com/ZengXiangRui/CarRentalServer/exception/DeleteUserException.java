package com.ZengXiangRui.CarRentalServer.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DeleteUserException extends RuntimeException {
    public DeleteUserException(String message) {
        super(message);
    }
}
