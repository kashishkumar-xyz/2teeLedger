# Data Model: Android UI

**Date**: 2025-10-18

## Source of Truth

The single source of truth for the application's data model is the Rust core library. The Android UI does not define its own data structures for business objects; instead, it uses data structures that are directly mapped from the Rust FFI layer.

Refer to the Rust data model defined in `ledger/src/models.rs` for the canonical definition of entities like `Transaction`.

## FFI Data Structures

The Android application will interact with FFI-safe versions of the Rust structs. These structs are defined in `ledger/src/ffi.rs` and are exposed to the UI via the JNA interface.

The primary data structures exposed to the UI will be:

- **`BalanceSummary`**: Represents the financial standing with a person.
  - `person_name`: `String`
  - `net_balance`: `f64`

- **`TransactionDetails`**: Represents a single financial event.
  - `person_name`: `String`
  - `amount`: `f64`
  - `date`: `i64` (Unix timestamp)
  - `note`: `Option<String>`

These structs will have corresponding `Structure` classes in the JNA interface on the Android side. The `LedgerRepository.kt` will be responsible for converting these FFI structures into Kotlin data classes that the ViewModel and UI can easily consume.