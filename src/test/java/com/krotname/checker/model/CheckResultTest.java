package com.krotname.checker.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckResultTest {
    @Test
    void shouldBuildValidFactoryResults() {
        CheckResult invalid = CheckResult.invalidInput("x");
        CheckResult notFound = CheckResult.notFound("x");
        CheckResult unavailable = CheckResult.serviceUnavailable("x", "err");
        CheckResult active = CheckResult.active("x", "ACTIVE");
        CheckResult notActive = CheckResult.notActive("x", "BANKRUPT");

        assertEquals(CompanyStatus.INVALID_INPUT, invalid.status());
        assertEquals(CompanyStatus.NOT_FOUND, notFound.status());
        assertEquals(CompanyStatus.SERVICE_UNAVAILABLE, unavailable.status());
        assertEquals(CompanyStatus.ACTIVE, active.status());
        assertEquals(CompanyStatus.NOT_ACTIVE, notActive.status());
        assertEquals("x", unavailable.inn());
        assertTrue(unavailable.message().contains("err"));
    }
}

