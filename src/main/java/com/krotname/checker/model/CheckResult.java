package com.krotname.checker.model;

public record CheckResult(
        String inn,
        CompanyStatus status,
        String dadataStatus,
        String message
) {
    public static CheckResult invalidInput(String inn) {
        return new CheckResult(inn, CompanyStatus.INVALID_INPUT, null, "ИНН должен содержать 10 или 12 цифр.");
    }

    public static CheckResult notFound(String inn) {
        return new CheckResult(inn, CompanyStatus.NOT_FOUND, null, "Организация не найдена.");
    }

    public static CheckResult serviceUnavailable(String inn, String reason) {
        return new CheckResult(inn, CompanyStatus.SERVICE_UNAVAILABLE, null, "Ошибка интеграции с DaData: " + reason);
    }

    public static CheckResult active(String inn, String rawStatus) {
        return new CheckResult(inn, CompanyStatus.ACTIVE, rawStatus, "Организация активна.");
    }

    public static CheckResult notActive(String inn, String rawStatus) {
        return new CheckResult(inn, CompanyStatus.NOT_ACTIVE, rawStatus, "Организация не активна.");
    }

    public boolean isActive() {
        return status.isActive();
    }
}
