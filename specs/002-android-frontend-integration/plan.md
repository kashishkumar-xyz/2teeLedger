# Implementation Plan: Android UI for teeLedger

**Branch**: `spec-002/android-frontend-integration` | **Date**: 2025-10-18 | **Spec**: /home/kaz/Dev/rust/_current/2teeLedger/specs/002-android-frontend-integration/spec.md

## Summary

This plan outlines the development of an Android frontend for the teeLedger Rust core. The Android application will serve as a view layer, interacting with the underlying Rust library via a Foreign Function Interface (FFI). This approach ensures that all business logic, database management, and encryption are handled by the secure, audited, and performant Rust core, in alignment with the project's security-first principles.

## Technical Context

**Language/Version**: Kotlin 1.9+
**Primary Dependencies**:
- **UI**: Jetpack Compose, AndroidX ViewModel
- **FFI**: JNA (Java Native Access) to call Rust functions.
- **Concurrency**: Kotlin Coroutines to manage calls to the Rust backend.
**Storage**: The Rust core library is solely responsible for creating, managing, and accessing the encrypted SQLite database using SQLCipher. The Android app does not directly access the database.
**Testing**: JUnit, Espresso, Mockito for the Android UI. Integration tests will cover the FFI boundary.
**Target Platform**: Android API Level 24+ (Nougat).
**Project Type**: Android Application (APK) that dynamically links the compiled Rust library (`libledger.so`).
**Constraints**:
- The Android application MUST NOT contain any business logic. All logic must be delegated to the Rust core.
- All data access MUST go through the FFI layer.
- The UI MUST remain responsive by performing Rust calls on background threads.
**Research Topics**:
- Best practices for exposing a C-compatible FFI from the Rust library.
- Using JNA on Android to load and interact with the `.so` shared library.
- Struct and data type mapping between Rust, C, and Kotlin/Java.
- Error handling and propagation across the FFI boundary.

## Constitution Check

*GATE: Must pass before Phase 0 research.*

- [X] **Security-First Design**: This architecture centralizes all sensitive operations within the Rust core, which is designed for security. The Android layer has no direct access to the database or encryption keys.
- [X-I] **Inconsistency Found**: The `ffi_ui_bindings.md` contradicts `spec.md` on whether the Android app is standalone. **Action**: This plan corrects the inconsistency. `spec.md` is the source of truth; the Android app is a frontend, not standalone. The FFI bindings will be updated to reflect this.
- [X] **Test-First Development**: The plan includes testing at all layers: unit tests for UI components in Android, and integration tests for the FFI boundary.
- [X] **CLI Interface**: The core logic is in the Rust library, which already exposes a CLI. This plan does not violate that principle.

*No violations identified post-correction.*

## Project Structure

### Documentation (this feature)

```
specs/002-android-frontend-integration/
├── plan.md              # This file
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
└── contracts/
    └── ffi_ui_bindings.md # Defines the Rust -> Kotlin FFI contract
```

### Source Code (repository root)

```
android/
└── app/
    ├── build.gradle.kts
    ├── jniLibs/           # To hold the compiled .so files
    │   ├── arm64-v8a/
    │   │   └── libledger.so
    │   └── x86_64/
    │       └── libledger.so
    └── src/
        └── main/
            ├── java/com/example/ledger/
            │   ├── MainActivity.kt
            │   ├── ffi/
            │   │   ├── LedgerApi.java      # JNA Interface definition
            │   │   └── LedgerRepository.kt # Kotlin wrapper for the FFI
            │   ├── viewmodel/
            │   │   └── LedgerViewModel.kt
            │   └── ui/
            │       ├── BalanceScreen.kt
            │       └── AddTransactionScreen.kt
            └── AndroidManifest.xml
ledger/
└── src/
    └── ffi.rs # Rust implementation of the FFI
```

## Development Phases

1.  **Phase 1: FFI Contract & Rust Implementation**: Define the C-compatible API in `ffi.rs`. Implement the functions required by the UI, such as `get_balances`, `add_transaction`, and `get_transaction_history`. Compile the Rust code into a shared library (`.so`) for Android.
2.  **Phase 2: Android FFI Integration**: Set up JNA in the Android project. Create the `LedgerApi.java` interface and the `LedgerRepository.kt` wrapper to call the Rust functions. Write integration tests to verify that data can be passed and received correctly across the FFI boundary.
3.  **Phase 3: UI and ViewModel**: Build the Jetpack Compose UI screens and the ViewModel. Connect the UI to the `LedgerRepository` to display data from the Rust core and to send user actions back to it.

## Complexity Tracking

*No constitution violations to justify.*