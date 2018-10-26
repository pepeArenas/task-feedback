package com.ns.task.exceptions;

public class ProductManagementException extends RuntimeException {

    public ProductManagementException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
