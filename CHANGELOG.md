# Changelog

## Unreleased

- Upgraded the embedded UI from a minimal form to a dashboard-style operational demo.
- Added CycloneDX SBOM generation and attached SBOM artifacts to CI/release flows.
- Added GitHub issue forms for bug reports and feature requests.
- Added Dependency Review workflow for pull-request dependency risk checks.
- Updated GitHub Actions to current major versions where appropriate.
- Added release artifact provenance and SBOM attestations.
- Updated Maven test/build plugins and JUnit to current Dependabot-recommended versions.
- Added architecture and quality-gate documentation for faster external review.
- Added README UI preview asset.

## 1.1.0

- Added public-facing quality surface:
  - Bilingual README (RU/EN) with architecture, run, API, and quality sections.
  - Coverage and test-tier workflow metadata (unit/integration/ui tags and Maven profiles).
  - CI hardening via GitHub Actions (verify, CodeQL, Scorecard, Dependabot, release).
  - Docker runtime artifacts and OpenAPI contract.
  - Additional comments for non-obvious logic paths.
  - Clarified config loading and startup behavior in source-level documentation.
- Improved score-focused repository docs:
  - Added `CODE_OF_CONDUCT.md`.
  - Added `CHANGELOG.md`.
