# FFI Contract: Android to Rust Core

**Branch**: `spec-002/android-frontend-integration` | **Date**: 2025-10-17

## Overview

This document defines the functional contract for a future Foreign Function Interface (FFI) between the Android UI (Kotlin) and the Rust core library (`libledgercore.so`).

Unlike the original architecture, the FFI bridge is **not** the primary mechanism for database interaction. The Android application is a standalone entity that manages its own encrypted database via Room and SQLCipher.

The purpose of this FFI contract is to define a limited set of high-value functions that can be exposed from the Rust library to the Android app. This could include:

-   Complex, CPU-intensive calculations.
-   Data import/export logic.
-   Synchronization with a remote backend.

## FFI Design Principles

-   **Stateless**: Functions exposed over FFI should be as stateless as possible.
-   **Limited Scope**: The FFI should not expose raw database access. It should provide specific, high-level functionality.
-   **Clear Data Ownership**: Data ownership and memory management must be clearly defined to prevent memory leaks or crashes.

## Example (Future Implementation)

```rust
// In Rust library (lib.rs)
#[no_mangle]
pub extern "C" fn perform_complex_calculation(input: i32) -> i32 {
    // ... complex logic ...
    input * 2
}
```

```kotlin
// In Android (e.g., LedgerCore.kt)
// This would be loaded via System.loadLibrary("ledgercore")
interface LedgerCoreFFI {
    fun perform_complex_calculation(input: Int): Int
}
```