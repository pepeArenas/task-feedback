package com.ns.task.exceptions;

public class ProductManagementApiError {
    private final String message;
    private final String debugMessage;

    public ProductManagementApiError(String message, Throwable exception) {
        this.message = message;
        this.debugMessage = exception.getLocalizedMessage();
    }
}

