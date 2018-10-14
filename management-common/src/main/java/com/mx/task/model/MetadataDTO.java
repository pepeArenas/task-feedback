package com.mx.task.model;

import java.util.StringJoiner;

public class MetadataDTO {

    private String cause;
    private String message;
    private String responsibleClass;

    public MetadataDTO(String cause, String message, String classResponsable) {
        this.cause = cause;
        this.message = message;
        this.responsibleClass = classResponsable;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponsibleClass() {
        return responsibleClass;
    }

    public void setResponsibleClass(String responsibleClass) {
        this.responsibleClass = responsibleClass;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MetadataDTO.class.getSimpleName() + "[", "]")
                .add("cause='" + cause + "'")
                .add("message='" + message + "'")
                .add("responsibleClass='" + responsibleClass + "'")
                .toString();
    }
}
