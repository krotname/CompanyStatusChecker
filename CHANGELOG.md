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
- Added dedicated `ui-tests` and `contract-tests` Maven profiles.
- Added OpenAPI contract coverage for the documented HTTP surface.
- Updated repository links to the canonical `CompanyStatusChecker` GitHub location.
- Added Maven Wrapper 3.3.4 pinned to Maven 3.9.16 with distribution checksum verification.
- Switched CI, release, and documentation commands to the project-local Maven Wrapper.
- Added Docker image build and `/health` smoke test to CI.
- Added OCI metadata labels to the Docker image.
- Removed hardcoded JAR version from Docker and release automation.
- Added GHCR Docker image publishing to the tag release workflow.
- Made the release build start from a clean Maven target directory.

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
