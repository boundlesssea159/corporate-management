package com.application.corporatemanagement.domain.common.exceptions;

public class DirtyDataException extends RuntimeException{
    public DirtyDataException(String message) {
        super(message);
    }
}
