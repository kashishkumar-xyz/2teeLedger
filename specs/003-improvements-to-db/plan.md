# Implementation Plan: Database Improvements for Querying and Data Integrity

**Branch**: `003-improvements-to-db` | **Date**: 2025-10-16 | **Spec**: /home/kaz/Dev/rust/_current/2teeLedger/specs/003-improvements-to-db/spec.md
**Input**: Feature specification from `/specs/003-improvements-to-db/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/commands/plan.md` for the execution workflow.

## Summary

The primary requirement is to fix transaction filtering and balance calculation issues in the CLI application, which are currently broken due to the unavailability of the SQLite JSON1 extension (and thus `JSON_EXTRACT`) in the `sqlcipher` build. The technical approach is to store `person` and `date` as dedicated columns in the `transactions_history` table, allowing for efficient and correct querying without relying on `JSON_EXTRACT`.

## Technical Context

**Language/Version**: Rust 1.75
**Primary Dependencies**: `rusqlite`, `clap`, `serde`, `serde_json`, `zeroize`, `base64`, `hmac`, `sha2`, `rand`, `uuid`, `chrono`
**Storage**: SQLite (encrypted with `sqlcipher`)
**Testing**: `cargo test`
**Target Platform**: Android/GrapheneOS (ultimate), Linux (development)
**Project Type**: CLI application
**Performance Goals**: `list` operations filtered by `person` or `date` complete in under 100ms for 10,000 transactions.
**Constraints**: `sqlcipher` build currently lacks JSON1 extension.
**Scale/Scope**: Personal finance ledger.

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **I. Append-Only Ledger**: Pass. The changes maintain the append-only nature.
- **II. Perspective-Based Transactions**: Pass. No change to this principle.
- **III. Security-First Design**: Pass. Adding dedicated columns improves query security by reducing reliance on JSON parsing.
- **IV. Test-First Development (NON-NEGOTIABLE)**: Pass. This will be adhered to during implementation.
- **V. CLI Interface**: Pass. The changes are for the CLI.
- **VI. Minimal Overhead**: **Violation Justified**. Adding redundant columns (`person`, `date`) slightly increases overhead but is necessary to resolve a critical functional issue (`JSON_EXTRACT` unavailability) and significantly improves query performance.

## Project Structure

### Documentation (this feature)

```
specs/003-improvements-to-db/
├── plan.md              # This file (/speckit.plan command output)
├── research.md          # Phase 0 output (/speckit.plan command)
├── data-model.md        # Phase 1 output (/speckit.plan command)
├── quickstart.md        # Phase 1 output (/speckit.plan command)
├── contracts/           # Phase 1 output (/speckit.plan command)
└── tasks.md             # Phase 2 output (/speckit.tasks command - NOT created by /speckit.plan)
```

### Source Code (repository root)

```
src/
├── models/
├── services/
├── cli/
└── lib/

tests/
├── contract/
├── integration/
└── unit/
```

**Structure Decision**: The project follows a single project structure with `cli` and `ledger` crates. The `src/` directory contains the core logic, `cli/` contains the CLI application, and `ledger/` contains the library. Tests are organized into `unit/` and `integration/`.

## Complexity Tracking

*Fill ONLY if Constitution Check has violations that must be justified*

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| VI. Minimal Overhead (Redundant columns for `person` and `date`) | Necessary to enable correct filtering and balance calculations due to `JSON_EXTRACT` unavailability in `sqlcipher`. | Relying solely on JSON parsing for filtering is not feasible without `JSON_EXTRACT`. Building `sqlcipher` from source with JSON1 is significantly more complex and not aligned with the current development context (Android target). |
