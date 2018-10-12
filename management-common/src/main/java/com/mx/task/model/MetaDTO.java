package com.mx.task.model;

import java.util.StringJoiner;

public class MetaDTO {

    private String cause;
    private String message;
    private String classResponsable;

    public MetaDTO() {
    }

    public MetaDTO(String cause, String message, String classResponsable) {
        this.cause = cause;
        this.message = message;
        this.classResponsable = classResponsable;
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

    public String getClassResponsable() {
        return classResponsable;
    }

    public void setClassResponsable(String classResponsable) {
        this.classResponsable = classResponsable;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MetaDTO.class.getSimpleName() + "[", "]")
                .add("cause='" + cause + "'")
                .add("message='" + message + "'")
                .add("classResponsable='" + classResponsable + "'")
                .toString();
    }
}
