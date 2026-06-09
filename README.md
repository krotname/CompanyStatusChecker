# Checker Corporate

[![CI](https://github.com/krotname/Checker-corporate/actions/workflows/ci.yml/badge.svg)](https://github.com/krotname/Checker-corporate/actions/workflows/ci.yml)
[![CodeQL](https://github.com/krotname/Checker-corporate/actions/workflows/codeql.yml/badge.svg)](https://github.com/krotname/Checker-corporate/actions/workflows/codeql.yml)
[![Scorecard](https://api.scorecard.dev/projects/github.com/krotname/Checker-corporate/badge)](https://securityscorecards.dev/viewer/?uri=github.com/krotname/Checker-corporate)
[![Coverage](https://codecov.io/gh/krotname/Checker-corporate/branch/main/graph/badge.svg)](https://codecov.io/gh/krotname/Checker-corporate)
[![Release](https://img.shields.io/github/v/release/krotname/Checker-corporate)](https://github.com/krotname/Checker-corporate/releases)
[![License](https://img.shields.io/github/license/krotname/Checker-corporate)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21%20LTS-007396)](https://openjdk.org/projects/jdk/21/)

## RU

### Что это
`Checker Corporate` — это демонстрационный Java-сервис для валидации ИНН юрлица и проверки его статуса через DaData API. Проект показывает практический подход к:

- валидации входных данных до сетевых вызовов;
- устойчивой обработке интеграционных ошибок;
- выделению доменной модели (`CheckResult`, `CompanyStatus`);
- предоставлению CLI и HTTP UI;
- автоматизации качества через GitHub Actions.

### Что внутри

- `com.krotname.checker.validation` — проверка ИНН и контрольных сумм.
- `com.krotname.checker.client` — HTTP-клиент и парсер ответа DaData.
- `com.krotname.checker.config` — безопасная загрузка конфигурации.
- `com.krotname.checker.ui` — HTTP-сервер (`/`, `/health`, `/api/check`).
- `src/main/resources/static/index.html` — минимальный UI для ручной проверки.

### Запуск

1. Установить Java 21 и Maven 3.9+.
2. Подготовить токен:

```bash
cp src/main/resources/checker.example.properties src/main/resources/checker.properties
# token=<Ваш токен DADATA>
```

или:

```bash
set DADATA_TOKEN=your_token   # Windows
export DADATA_TOKEN=your_token # macOS/Linux
```

#### CLI

```bash
mvn -q -DskipTests package
java -jar target/checker-corporate-1.1.0.jar 9710083390
```

#### Веб UI

```bash
mvn -q -DskipTests package
java -jar target/checker-corporate-1.1.0.jar --server 8080
```

Откройте `http://localhost:8080`.

### API

- `GET /health` — health-check для автоматизации.
- `GET /api/check?inn=<ИНН>` — статус компании.

Пример ответа:

```json
{
  "inn": "9710083390",
  "status": "ACTIVE",
  "dadataStatus": "ACTIVE",
  "message": "Организация активна."
}
```

### Тестовая стратегия

- **Unit**: валидация ИНН, маппинг статусов DaData, конфигурация, доменные фабрики.
- **Интеграционные**: HTTP-клиент DaData с локальным тестовым сервером + маршруты API.
- **UI**: smoke-проверки `/`, `/api/check`, `/health`, включая ошибки валидации.
- **Contract/через интеграционный слой**: формат `DadataResponseParser` и поведение `CheckerCorporate.check`.

### Качество и автоматизация

- `CI` (`.github/workflows/ci.yml`) — `mvn verify`.
- `JaCoCo` с минимальным порогом покрытия `LINE >= 0.80`.
- `Checkstyle` на этапе `verify`.
- `CodeQL` и `OpenSSF Scorecard`.
- `Dependabot`, `Release` workflow.

```bash
mvn -q test
mvn -q verify
```

---

## EN

### What it is
`Checker Corporate` is a production-oriented Java showcase that validates company INN and returns status from DaData.
It is designed to demonstrate clean layering, testability, and CI quality posture.

### What’s inside

- `validation` — INN format and checksum validation.
- `client` — DaData HTTP transport and response parser.
- `config` — runtime configuration loading.
- `ui` — tiny HTTP server for `/`, `/health`, `/api/check`.
- `static/index.html` — lightweight browser UI.

### Run

```bash
cp src/main/resources/checker.example.properties src/main/resources/checker.properties
# token=<YOUR_DADATA_TOKEN>

mvn -q -DskipTests package
java -jar target/checker-corporate-1.1.0.jar 9710083390
```

### API

- `GET /health` — automation endpoint.
- `GET /api/check?inn=<INN>` — company status in JSON.

### Quality and reviewability

- Structured package layout with small, focused classes.
- Multiple test categories (unit/integration/ui/contract).
- Security and release automation in GitHub Actions.
- Clear runtime setup: environment variable or properties resource.

### License

GPL-3.0 — [LICENSE](LICENSE).
