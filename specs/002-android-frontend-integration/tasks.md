# Feature Tasks: Android UI for teeLedger (FFI Architecture)

**Branch**: `spec-002/android-frontend-integration` | **Date**: 2025-10-18 | **Spec**: `specs/002-android-frontend-integration/spec.md`

This document outlines the implementation tasks for the teeLedger Android application, which will act as a frontend to the core Rust library via an FFI.

## Phase 1: Project Setup & FFI Prerequisites

*Environment and tooling setup for Rust and Android cross-compilation.*

- [X] T001 [P] Install Rust Android cross-compilation targets (`aarch64-linux-android`, `x86_64-linux-android`).
- [X] T002 [P] Install `cargo-ndk` to simplify the build process.
- [X] T003 Verify the base Android project can be built successfully in the `android/` directory.
- [X] T004 Add the JNA dependency to the `android/app/build.gradle.kts` file.
- [ ] T005 [CHECKPOINT] Perform a full build in Android Studio to ensure the project compiles successfully.

## Phase 2: Foundational - FFI Contract & Rust Implementation

*Build the FFI bridge in the Rust core. This is the foundation for all UI features.*

- [ ] T006 Define the FFI-safe data structures (`Transaction`, `Balance`) in `ledger/src/ffi.rs` as specified in `contracts/ffi_ui_bindings.md`.
- [ ] T007 Implement the `open_database` FFI function in `ledger/src/ffi.rs`.
- [ ] T008 [FR-004] [TEST] Add input validation to the `add_transaction` FFI function in `ledger/src/ffi.rs` to handle empty person names and zero/invalid amounts.
- [ ] T009 Implement the `add_transaction` FFI function in `ledger/src/ffi.rs`.
- [ ] T010 Implement the `get_all_balances` FFI function in `ledger/src/ffi.rs`.
- [ ] T011 Implement the `get_transactions_for_person` FFI function in `ledger/src/ffi.rs`.
- [ ] T012 Implement the memory management functions (`free_string`, `free_balance_list`, `free_transaction_list`) in `ledger/src/ffi.rs`.
- [ ] T013 Implement the `get_last_error` function for thread-local error handling in `ledger/src/ffi.rs`.
- [ ] T014 [P] [TEST] Write integration tests in Rust to verify the behavior of all exported FFI functions.
- [ ] T015 Compile the Rust library into `.so` files for all target Android architectures and place them in `android/app/src/main/jniLibs/`.
- [ ] T016 [CHECKPOINT] Perform a full build in Android Studio to ensure the project compiles successfully.

## Phase 3: Foundational - Secure Key Management

*Implement secure storage for the database encryption key using the Android Keystore.*

- [ ] T017 [FR-005] Create a `KeyManager` class in `android/app/src/main/java/com/example/ledger/security/` that uses the Android Keystore to securely store and retrieve the database encryption key.
- [ ] T018 [FR-005] Implement logic in the `MainActivity` or a startup service to use the `KeyManager` to retrieve the key and pass it to the `LedgerRepository` to open the database.
- [ ] T019 [P] [TEST] Write unit tests for the `KeyManager` class.
- [ ] T020 [CHECKPOINT] Perform a full build in Android Studio to ensure the project compiles successfully.

## Phase 4: Foundational - Android FFI Integration

*Connect the Android app to the compiled Rust library.*

- [ ] T021 Create the JNA interface `LedgerApi.java` in `android/app/src/main/java/com/example/ledger/ffi/` to map all the Rust FFI functions.
- [ ] T022 Create a `LedgerRepository.kt` class in `android/app/src/main/java/com/example/ledger/ffi/` that provides a clean, coroutine-based Kotlin API over the `LedgerApi.java` interface.
- [ ] T023 Implement the logic in `LedgerRepository.kt` to call the Rust functions, handle data type conversions, and manage memory (calling the `free_*` functions).
- [ ] T024 [P] [TEST] Write integration tests for the `LedgerRepository.kt` to ensure data can be passed to and received from the Rust library correctly.
- [ ] T025 [CHECKPOINT] Perform a full build in Android Studio to ensure the project compiles successfully.

## Phase 5: User Story 1 - View Balances

*Goal: As a user, I want to open the app and immediately see a summary of how much each person owes me or I owe them.*

- [ ] T026 [US1] Create a `BalanceViewModel.kt` that uses the `LedgerRepository` to fetch the list of balances via a public `StateFlow` in `android/app/src/main/java/com/example/ledger/viewmodel/`.
- [ ] T027 [P] [TEST] Write unit tests for `BalanceViewModel`.
- [ ] T028 [P] [US1] Create a `BalanceListItem` Composable to display a single person's name and balance in `android/app/src/main/java/com/example/ledger/ui/BalanceScreen.kt`.
- [ ] T029 [US1] Create the main `BalanceScreen` Composable that observes the `BalanceViewModel` and displays a `LazyColumn` of `BalanceListItem` Composables.
- [ ] T030 [US1] Update `MainActivity.kt` to display the `BalanceScreen` and provide it with the `BalanceViewModel`.
- [ ] T031 [CHECKPOINT] Perform a full build in Android Studio to ensure the project compiles successfully.

## Phase 6: User Story 2 - Add a New Transaction

*Goal: As a user, I want to easily add a new financial transaction from the main screen.*

- [ ] T032 [P] [US2] Create an `AddTransactionScreen` Composable containing `TextField`s for person, amount, and an optional note, along with a 'Save' button, in `android/app/src/main/java/com/example/ledger/ui/AddTransactionScreen.kt`.
- [ ] T033 [US2] Add a `saveTransaction` method to the `BalanceViewModel` that calls the corresponding method in `LedgerRepository`.
- [ ] T034 [P] [TEST] Write unit tests for the `saveTransaction` logic in the `BalanceViewModel`.
- [ ] T035 [US2] Add a Floating Action Button to the `BalanceScreen` to navigate to the `AddTransactionScreen`.
- [ ] T036 [US2] Implement the save logic in the `AddTransactionScreen` to call the `BalanceViewModel`, which in turn calls the FFI function.
- [ ] T037 [CHECKPOINT] Perform a full build in Android Studio to ensure the project compiles successfully.

## Phase 7: User Story 3 - View Transaction History

*Goal: As a user, I want to tap on a person's name in the balance list to see a detailed history of all our transactions.*

- [ ] T038 [P] [US3] Create a `TransactionHistoryScreen` Composable that displays a `LazyColumn` of `Transaction` items in `android/app/src/main/java/com/example/ledger/ui/TransactionHistoryScreen.kt`.
- [ ] T039 [US3] Create a `TransactionHistoryViewModel` that can fetch all transactions for a specific person from the `LedgerRepository` in `android/app/src/main/java/com/example/ledger/viewmodel/`.
- [ ] T040 [P] [TEST] Write unit tests for `TransactionHistoryViewModel`.
- [ ] T041 [US3] Implement navigation from the `BalanceScreen` to the `TransactionHistoryScreen`, passing the selected person's name when an item is tapped.
- [ ] T042 [CHECKPOINT] Perform a full build in Android Studio to ensure the project compiles successfully.

## Phase 8: Polish & Cross-Cutting Concerns

*Final polish and non-functional requirements.*

- [ ] T043 [NFR] Implement loading indicators in the UI while waiting for FFI calls to complete.
- [ ] T044 [NFR] Implement UI handling for FFI errors (e.g., from `get_last_error`) and display user-friendly messages.
- [ ] T045 [TEST] Write UI tests (Espresso) for the 'Add Transaction' workflow (US2).
- [ ] T046 [TEST] Write UI tests (Espresso) for the 'View Balances' and 'View History' workflows (US1, US3).
- [ ] T047 [CHECKPOINT] Perform a full build in Android Studio to ensure the project compiles successfully.

---

## Dependencies

- **Phase 5 (US1)** depends on **Phase 2, 3 & 4**.
- **Phase 6 (US2)** depends on **Phase 5**.
- **Phase 7 (US3)** depends on **Phase 5**.

## Implementation Strategy

The strategy is to build the FFI bridge and security components first (Phases 2, 3 & 4), as they are the foundation for all functionality. Once the bridge is tested and stable, the UI for each user story can be built on top of it, starting with the highest priority feature (View Balances).