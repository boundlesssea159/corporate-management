package com.application.corporatemanagement.adapter.driven.persistence.exceptions;

import javax.management.relation.RelationException;

public class ReflectException extends RuntimeException {
    public ReflectException(Exception e) {
        super(e);
    }
}
