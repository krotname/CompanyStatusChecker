package com.krotname.checker.model;

public enum CompanyStatus {
    ACTIVE,
    NOT_ACTIVE,
    NOT_FOUND,
    INVALID_INPUT,
    SERVICE_UNAVAILABLE,
    UNKNOWN;

    public boolean isActive() {
        return this == ACTIVE;
    }
}

