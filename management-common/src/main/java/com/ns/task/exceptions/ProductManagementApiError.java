package com.ns.task.exceptions;

class ProductManagementApiError {
    private final String message;
    private final String debugMessage;

    ProductManagementApiError(String message, Throwable exception) {
        this.message = message;
        this.debugMessage = exception.getLocalizedMessage();
    }
}

