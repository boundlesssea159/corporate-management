package com.application.corporatemanagement.domain.exceptions;

public class DirtyDataException extends RuntimeException{
    public DirtyDataException(String message) {
        super(message);
    }
}
