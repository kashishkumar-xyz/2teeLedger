# Implementation Plan: Core Rust Library Development

**Branch**: `001-core-rust-lib` | **Date**: 2025-10-15 | **Spec**: /home/kaz/Dev/rust/_current/teeLedger/specs/001-core-rust-lib/spec.md
**Input**: Feature specification from `/specs/001-core-rust-lib/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/commands/plan.md` for the execution workflow.

## Summary

This plan outlines the development of the core Rust library for teeLedger, focusing on defining data structures, implementing ledger logic (add, list, balance), integrating with SQLite, exposing functionality via FFI/JNI, and comprehensive unit testing.

## Technical Context

**Language/Version**: Rust 1.75+  
**Primary Dependencies**: rusqlite, jni (for FFI/JNI interface), zeroize, base64, hmac (for checksums)  
**Storage**: SQLCipher-encrypted SQLite in app private storage (WAL mode enabled). Key management via Android Keystore (KEK) and encrypted DEK.  
**Testing**: cargo test  
**Target Platform**: Android (via JNI)  
**Project Type**: Library (cdylib)  
**Performance Goals**: Transaction recording < 2 seconds; Balance calculation < 3 seconds for 10,000 transactions; Backup/restore operations < 5 seconds for typical ledger size.  
**Constraints**: Offline-capable, minimal memory footprint, strong security, robust data recovery.  
**Scale/Scope**: Up to 10,000 transactions, single-user local ledger.

### Backup & Recovery Strategy
The system employs a layered backup and recovery strategy:
- **Fast Recovery**: Regular encrypted file-level snapshots saved atomically in app-private backups folder.
- **Fine-grained History**: Append-only audit/version tables for critical entities (transactions) to reconstruct recent logical state.
- **Corruption Detection**: `PRAGMA integrity_check` at startup and periodically.
- **Automated Remediation**: Attempt automated recovery from latest valid local backup; if failed, notify user and offer revert to last healthy state (application-managed VCS).

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] Append-Only Ledger: Ensure data structure supports immutable additions only
- [x] Perspective-Based Transactions: Verify transaction recording follows user viewpoint (+/-)
- [x] Security-First Design: Confirm parameterized queries, input validation, least privileges
- [x] Minimal Overhead: Validate minimal dependencies and resource usage
- [x] SQLite Integration: Check for mature bindings and encryption support

## Project Structure

### Documentation (this feature)

```
specs/001-core-rust-lib/
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
├── lib.rs               # Main library entry point
├── models.rs            # Defines Transaction and Balance structs
├── db.rs                # SQLite interaction logic
├── ffi.rs               # FFI/JNI interface
├── key_management.rs    # Handles DEK generation, Keystore wrapping/unwrapping
├── backup.rs            # Implements online backup API, atomic snapshots
└── recovery.rs          # Handles integrity checks, automated recovery, rollback

cli/
└── src/
    └── main.rs          # CLI application entry point

data/
└── backups/             # Directory for encrypted DB snapshots

tests/
├── unit/
│   ├── models_test.rs
│   ├── db_test.rs
│   ├── ffi_test.rs
│   ├── key_management_test.rs
│   ├── backup_test.rs
│   └── recovery_test.rs
└── integration/
    ├── ledger_integration_test.rs
    └── cli_test.rs      # CLI integration tests
```

## Complexity Tracking

*Fill ONLY if Constitution Check has violations that must be justified*

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| | Layered Backup & Recovery | Ensures high data integrity and recoverability in a secure, offline environment. | Simpler file copying or no recovery mechanisms would compromise security and data loss prevention, which are core project principles. |
| | CLI Integration | Provides a robust interface for early testing, debugging, and post-release support, aligning with the "CLI Interface" principle. | Deferring CLI development would hinder early development feedback and limit post-release diagnostic capabilities. |
