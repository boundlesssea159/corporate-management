package com.application.corporatemanagement.adapter.driven.persistence.exceptions;

public class QueryException extends RuntimeException {
    public QueryException(Exception e) {
        super(e);
    }
}
