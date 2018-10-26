package com.ns.task.exceptions;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ProductManagerExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
     *
     * @param exception the DataIntegrityViolationException
     * @return the ProductManagementApiError object
     */
    @ExceptionHandler(ProductManagementException.class)
    protected ResponseEntity handleDuplicateProduct(ProductManagementException exception) {
        if (exception.getCause() instanceof ConstraintViolationException) {
            return buildResponseEntity(new ProductManagementApiError("Constraint error", exception.getCause()));
        }
        return buildResponseEntity(new ProductManagementApiError("Duplicate error", exception));
    }

    private ResponseEntity<Object> buildResponseEntity(ProductManagementApiError productManagementApiError) {
        return new ResponseEntity<>(productManagementApiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
