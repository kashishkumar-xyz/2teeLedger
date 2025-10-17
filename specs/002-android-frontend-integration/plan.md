# Implementation Plan: Android UI for teeLedger

**Branch**: `spec-002/android-frontend-integration` | **Date**: 2025-10-17 | **Spec**: /home/kaz/Dev/rust/_current/2teeLedger/specs/002-android-frontend-integration/spec.md

## Summary

This plan outlines the development of a robust, standalone Android application for teeLedger. It prioritizes stability and security by using modern, industry-standard Android practices. The application will manage its own encrypted database and will be architected to allow for future integration with the Rust core library.

## Technical Context

**Language/Version**: Kotlin 1.9+
**Primary Dependencies**:
- **UI**: Jetpack Compose, AndroidX ViewModel
- **Database**: AndroidX Room (for robust data access)
- **Encryption**: SQLCipher for Android (`net.zetetic:sqlcipher-android`) via Maven Central
- **Concurrency**: Kotlin Coroutines
**Storage**: The Android application will manage its own encrypted SQLite database using Room and SQLCipher. This ensures stability and leverages the modern Android persistence framework.
**Testing**: JUnit, Espresso, Mockito.
**Target Platform**: Android API Level 24+ (Nougat).
**Project Type**: Android Application (APK), developed as a standalone project within the `android/` directory.
**Constraints**: All data stored on the device MUST be encrypted using SQLCipher.
**Research Topics**:
- Best practices for structuring a Room database with SQLCipher.
- MVVM architecture patterns with Jetpack Compose and Flow.

## Constitution Check

*GATE: Must pass before Phase 0 research.*

- [X] **Security-First Design**: The plan mandates the use of SQLCipher via its official, pre-built library for all local data, ensuring encryption-at-rest.
- [X] **Test-First Development**: The plan includes standard Android testing frameworks to enable unit, integration, and UI testing.
- [X] **Minimal Overhead**: The dependency choices (AndroidX, Room, Compose) are the standard, recommended libraries for modern Android development, avoiding unnecessary third-party bloat.

*No violations identified.*

## Project Structure

### Documentation (this feature)

```
specs/002-android-frontend-integration/
├── plan.md              # This file
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
└── contracts/
    └── ffi_ui_bindings.md # Describes future FFI integration
```

### Source Code (repository root)

```
android/
└── app/
    ├── build.gradle.kts
    └── src/
        └── main/
            ├── java/com/example/ledger/
            │   ├── MainActivity.kt
            │   ├── viewmodel/
            │   │   └── LedgerViewModel.kt
            │   ├── model/
            │   │   └── Transaction.kt      # Room Entity
            │   ├── db/
            │   │   ├── AppDatabase.kt      # Room Database
            │   │   └── TransactionDao.kt   # Room DAO
            │   └── ui/
            │       ├── BalanceScreen.kt
            │       └── AddTransactionScreen.kt
            └── AndroidManifest.xml
```

## Development Phases

This project will be developed in phases to ensure stability at each step.

1.  **Phase 1: Database Layer**: Implement the Room database with SQLCipher encryption. Define entities and DAOs. Verify that the database can be created, opened, and written to.
2.  **Phase 2: UI and ViewModel**: Build the Jetpack Compose UI screens and the ViewModel. Connect the UI to the database via the ViewModel to display and modify data.
3.  **Phase 3: FFI Integration (Future)**: Once the standalone Android app is fully functional, tackle the FFI integration to connect to the Rust core library for specific, high-value functions (e.g., complex calculations, data synchronization), rather than as a dependency for all operations.

## Complexity Tracking

*No constitution violations to justify.*
