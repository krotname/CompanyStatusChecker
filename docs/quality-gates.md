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
| Style | Checkstyle runs during `verify` and includes test sources | `checkstyle.xml` + Maven plugin |
| CI | GitHub Actions runs `./mvnw -B clean verify` on push and PR | `.github/workflows/ci.yml` |
| Security scanning | CodeQL Java workflow | `.github/workflows/codeql.yml` |
| Supply chain | CycloneDX SBOM XML/JSON generated during package | `target/bom.xml`, `target/bom.json` |
| Dependency risk | Dependency Review for PR dependency changes | `.github/workflows/dependency-review.yml` |
| Release trust | GitHub artifact provenance and SBOM attestations | `.github/workflows/release.yml` |
| Repository health | Security policy, code of conduct, contribution guide, issue forms | root docs + `.github/ISSUE_TEMPLATE` |

## Review Checklist

Use this checklist before publishing larger changes:

- `./mvnw -q test`
- `./mvnw -q verify`
- README updated for user-facing behavior.
- OpenAPI contract updated for API behavior changes.
- No tokens, API keys, or local config files committed.
- New behavior has at least one focused unit or integration test.

## CI Expectations

The default branch should stay green on:

- `CI`
- `CodeQL`
- `OpenSSF Scorecard`
- `Dependency Review` for pull requests

GitHub Dependency Graph and vulnerability alerts are enabled so dependency review can run successfully on Dependabot and contributor pull requests.
