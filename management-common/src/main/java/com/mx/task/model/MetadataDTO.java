package com.mx.task.model;

import java.util.StringJoiner;

class MetadataDTO {

    private final String cause;
    private final String message;
    private final String responsibleClass;

    private MetadataDTO(String cause, String message, String responsibleClass) {
        this.cause = cause;
        this.message = message;
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
