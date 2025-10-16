# Implementation Plan: Android UI for teeLedger

**Branch**: `002-android-frontend-integration` | **Date**: 2025-10-16 | **Spec**: /home/kaz/Dev/rust/_current/2teeLedger/specs/002-android-frontend-integration/spec.md

## Summary

This plan outlines the development of the Android UI for the teeLedger application. It covers the technical stack, project structure, and the generation of design artifacts required to build a user-friendly interface that interacts with the backend Rust core library via JNI.

## Technical Context

**Language/Version**: Kotlin 1.9+
**Primary Dependencies**: AndroidX (AppCompat, Core KTX, ViewModel, LiveData), Google Material Components, Coroutines, JNA (for simpler JNI mapping).
**Storage**: The UI layer does not manage storage directly; it interacts with the Rust core library which handles the encrypted SQLite database.
**Testing**: JUnit 5, Espresso, Mockito.
**Target Platform**: Android API Level 26+ (Oreo).
**Project Type**: Android Application (APK).
**Performance Goals**: App launch to interactive < 2 seconds; transaction submission feedback < 500ms.
**Constraints**: Must integrate with the pre-existing Rust core library (`libledgercore.so`) via a JNI bridge. All business logic and data storage is delegated to the Rust core.
**Research Topics**:
- Best practices for modern Android JNI/JNA integration with a Rust library.
- Securely managing the lifecycle of the database key between the Android Keystore and the JNI boundary.

## Constitution Check

*GATE: Must pass before Phase 0 research.*

- [X] **Security-First Design**: The plan includes research into secure key handling at the JNI boundary.
- [X] **Test-First Development**: The plan includes standard Android testing frameworks (JUnit, Espresso) to enable UI and integration testing.
- [X] **Minimal Overhead**: The dependency choices (AndroidX, Coroutines) are standard and essential for modern Android development. JNA is proposed to reduce boilerplate JNI code, aligning with this principle.

*No violations identified.*

## Project Structure

### Documentation (this feature)

```
specs/002-android-frontend-integration/
├── plan.md              # This file
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
└── contracts/           # Phase 1 output
    └── ffi_ui_bindings.md
```

### Source Code (repository root)

```
app/
└── src/
    ├── main/
    │   ├── java/com/example/teeledger/
    │   │   ├── MainActivity.kt
    │   │   ├── viewmodel/
    │   │   │   └── LedgerViewModel.kt
    │   │   ├── ui/
    │   │   │   ├── BalanceScreen.kt
    │   │   │   ├── AddTransactionScreen.kt
    │   │   │   └── TransactionHistoryScreen.kt
    │   │   └── jni/
    │   │       └── LedgerCore.kt      # JNA Interface definition
    │   └── res/
    │       ├── layout/
    │       └── ...
    └── test/
        └── java/com/example/teeledger/
            └── ...
```

## Complexity Tracking

*No constitution violations to justify.*