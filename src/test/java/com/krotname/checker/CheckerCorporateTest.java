package com.krotname.checker;

import com.krotname.checker.client.DadataClient;
import com.krotname.checker.model.CheckResult;
import com.krotname.checker.model.CompanyStatus;
import com.krotname.checker.validation.InnValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CheckerCorporateTest {
    @Test
    void shouldReturnActiveWhenApiReportsActive() throws IOException, InterruptedException {
        CheckerCorporate checker = new CheckerCorporate(
                new AlwaysValidInnValidator(),
                stubClient("ACTIVE")
        );
        CheckResult result = checker.check("not-an-inn");
        assertEquals(CompanyStatus.ACTIVE, result.status());
        assertEquals("ACTIVE", result.dadataStatus());
        assertTrue(result.isActive());
    }

    @Test
    void shouldReturnNotActiveOnLiquidated() throws IOException, InterruptedException {
        CheckerCorporate checker = new CheckerCorporate(
                new AlwaysValidInnValidator(),
                stubClient("LIQUIDATED")
        );
        CheckResult result = checker.check("not-an-inn");
        assertEquals(CompanyStatus.NOT_ACTIVE, result.status());
        assertEquals("LIQUIDATED", result.dadataStatus());
        assertEquals("Организация не активна.", result.message());
    }

    @ParameterizedTest
    @CsvSource({
            "'ACTIVE',        true",
            "'LIQUIDATED',    false",
            "'BANKRUPT',      false"
    })
    void shouldMapDadataStatuses(String dadataStatus, boolean expectedActive) throws IOException, InterruptedException {
        CheckerCorporate checker = new CheckerCorporate(
                new AlwaysValidInnValidator(),
                stubClient(dadataStatus)
        );
        CheckResult result = checker.check("x");
        assertEquals(expectedActive, result.isActive());
    }

    @Test
    void shouldCreateCheckerUsingTokenFactory() {
        CheckerCorporate checker = CheckerCorporate.withToken("test-token");
        assertNotNull(checker);
    }

    @Test
    void shouldUseDefaultConstructorForValidationWithoutNetwork() {
        System.setProperty("DADATA_TOKEN", "token-from-test");
        try {
            CheckerCorporate checker = new CheckerCorporate();
            assertEquals(CheckResult.invalidInput("!!!").status(), checker.check("!!!").status());
        } finally {
            System.clearProperty("DADATA_TOKEN");
        }
    }

    @Test
    void shouldEvaluateIsActiveWithoutNetwork() {
        System.setProperty("DADATA_TOKEN", "token-from-test");
        try {
            assertFalse(CheckerCorporate.isActive("bad-inn"));
        } finally {
            System.clearProperty("DADATA_TOKEN");
        }
    }

    private DadataClient stubClient(String value) {
        return inn -> Optional.of(value);
    }

    private static class AlwaysValidInnValidator extends InnValidator {
        @Override
        public boolean isValid(String inn) {
            return true;
        }
    }
}
