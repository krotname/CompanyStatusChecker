# Quality Gates

This repository is optimized for quick external review: each quality claim is tied to an executable or visible artifact.

| Area | Evidence | Command or location |
| --- | --- | --- |
| Unit tests | Domain and validation checks tagged with `@Tag("unit")` | `./mvnw -q test -Punit-tests` |
| Integration tests | HTTP client and embedded API server tests tagged with `@Tag("integration")` | `./mvnw -q test -Pintegration-tests` |
| UI smoke | Embedded HTTP server route checks tagged with `@Tag("ui")` | `./mvnw -q test -Pui-tests` |
| Contract tests | DaData parser and OpenAPI checks tagged with `@Tag("contract")` | `./mvnw -q test -Pcontract-tests` |
| Full verification | Tests, package, SBOM, Checkstyle, JaCoCo gate | `./mvnw -q verify` |
| Coverage | JaCoCo line coverage threshold `LINE >= 0.80` | `pom.xml` |
| Mutation testing | PIT mutation score threshold `mutationThreshold >= 80` | `./mvnw -q verify -Pmutation-tests` |
| Style | Checkstyle runs during `verify` and includes test sources | `checkstyle.xml` + Maven plugin |
| Static bug analysis | SpotBugs runs during `verify` with `effort=Max`, `threshold=Low`, and fail-on-error enabled | `pom.xml` |
| CI | GitHub Actions runs category test jobs plus full `./mvnw -B clean verify` on push and PR | `.github/workflows/ci.yml` |
| Test reports | CI uploads Surefire reports for unit, integration, UI, and contract jobs | `.github/workflows/ci.yml` |
| Docker runtime | CI builds the image and checks `/health` from a running container | `.github/workflows/ci.yml` |
| Static analysis reports | CI uploads the SpotBugs XML report | `target/spotbugsXml.xml` |
| Mutation reports | Dedicated workflow publishes PIT HTML/XML reports | `.github/workflows/mutation-testing.yml` |
| Security scanning | CodeQL Java workflow | `.github/workflows/codeql.yml` |
| Supply chain | CycloneDX SBOM XML/JSON generated during package | `target/bom.xml`, `target/bom.json` |
| Dependency risk | Dependency Review for PR dependency changes | `.github/workflows/dependency-review.yml` |
| Release trust | GitHub artifact provenance, SBOM attestations, and GHCR Docker image publishing | `.github/workflows/release.yml` |
| Repository health | Security policy, code of conduct, contribution guide, issue forms | root docs + `.github/ISSUE_TEMPLATE` |

## Review Checklist

Use this checklist before publishing larger changes:

- `./mvnw -q test`
- `./mvnw -q verify`
- `./mvnw -q verify -Pmutation-tests`
- README updated for user-facing behavior.
- OpenAPI contract updated for API behavior changes.
- No tokens, API keys, or local config files committed.
- New behavior has at least one focused unit or integration test.

## CI Expectations

The default branch should stay green on:

- `CI`
- `Mutation Testing`
- `SpotBugs` through the default CI verification job
- `CodeQL`
- `OpenSSF Scorecard`
- `Dependency Review` for pull requests

GitHub Dependency Graph and vulnerability alerts are enabled so dependency review can run successfully on Dependabot and contributor pull requests.
