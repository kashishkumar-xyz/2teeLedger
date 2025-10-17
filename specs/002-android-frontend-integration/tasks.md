# Feature Tasks: Android UI for teeLedger

**Branch**: `spec-002/android-frontend-integration` | **Date**: 2025-10-17 | **Spec**: `specs/002-android-frontend-integration/spec.md`

This document outlines the implementation tasks for the teeLedger Android application based on the revised, standalone architecture.

## Phase 1: Project Setup & Structure

*Initial project scaffolding and directory creation.*

- [ ] T001 Verify the base Android project can be built successfully in the `android/` directory.
- [ ] T002 Create the directory structure for the application source code: `android/app/src/main/java/com/example/ledger/{model,db,ui,viewmodel}`.

## Phase 2: Foundational - Database Layer

*Build the secure, encrypted database foundation using Room and SQLCipher. This phase is critical for security and stability.*

- [ ] T003 Define the `Transaction` data class as a Room entity in `android/app/src/main/java/com/example/ledger/model/Transaction.kt`.
- [ ] T004 Create the `TransactionDao` interface with methods for `insert`, `getAll`, and `getByPerson` in `android/app/src/main/java/com/example/ledger/db/TransactionDao.kt`.
- [ ] T005 Create the `AppDatabase` abstract class, inheriting from `RoomDatabase`, in `android/app/src/main/java/com/example/ledger/db/AppDatabase.kt`.
- [ ] T006 Implement the SQLCipher `SupportOpenHelperFactory` when building the Room database instance to enable encryption. This should be handled within a database provider object or companion object in `AppDatabase.kt`.
- [ ] T007 Create a `LedgerRepository` class that takes the `TransactionDao` as a dependency to abstract all data operations from the ViewModels, in `android/app/src/main/java/com/example/ledger/db/LedgerRepository.kt`.

## Phase 3: User Story 1 - View Balances

*Goal: As a user, I want to open the app and immediately see a summary of how much each person owes me or I owe them.*

- [ ] T008 [US1] Create a `Balance` data class to hold aggregated summary data (`person_name`, `net_balance`) in `android/app/src/main/java/com/example/ledger/model/Balance.kt`.
- [ ] T009 [US1] Implement a method in `LedgerRepository` to query all transactions and compute a `List<Balance>`.
- [ ] T010 [US1] Create a `BalanceViewModel` that uses the `LedgerRepository` to fetch the list of balances via a public `StateFlow` in `android/app/src/main/java/com/example/ledger/viewmodel/BalanceViewModel.kt`.
- [ ] T011 [P] [US1] Create a `BalanceListItem` Composable to display a single person's name and balance in `android/app/src/main/java/com/example/ledger/ui/BalanceScreen.kt`.
- [ ] T012 [US1] Create the main `BalanceScreen` Composable that observes the `BalanceViewModel` and displays a `LazyColumn` of `BalanceListItem` Composables.
- [ ] T013 [US1] Update `MainActivity.kt` to display the `BalanceScreen` and provide it with the `BalanceViewModel`.

## Phase 4: User Story 2 - Add a New Transaction

*Goal: As a user, I want to easily add a new financial transaction from the main screen.*

- [ ] T014 [P] [US2] Create an `AddTransactionScreen` Composable containing `TextField`s for person, amount, and an optional note, along with a 'Save' button, in `android/app/src/main/java/com/example/ledger/ui/AddTransactionScreen.kt`.
- [ ] T015 [US2] Add a `saveTransaction` method to the `LedgerRepository` and `BalanceViewModel`.
- [ ] T016 [US2] Add a Floating Action Button to the `BalanceScreen` to navigate to the `AddTransactionScreen`.
- [ ] T017 [US2] Implement the save logic in the `BalanceViewModel` to be called from the `AddTransactionScreen`, persisting the new transaction and navigating back.

## Phase 5: User Story 3 - View Transaction History

*Goal: As a user, I want to tap on a person's name in the balance list to see a detailed history of all our transactions.*

- [ ] T018 [P] [US3] Create a `TransactionHistoryScreen` Composable that displays a `LazyColumn` of `Transaction` items in `android/app/src/main/java/com/example/ledger/ui/TransactionHistoryScreen.kt`.
- [ ] T019 [US3] Create a `TransactionHistoryViewModel` that can fetch all transactions for a specific person from the `LedgerRepository` in `android/app/src/main/java/com/example/ledger/viewmodel/TransactionHistoryViewModel.kt`.
- [ ] T020 [US3] Implement navigation from the `BalanceScreen` to the `TransactionHistoryScreen`, passing the selected person's name when an item is tapped.

## Phase 6: Polish & Cross-Cutting Concerns

*Final polish and non-functional requirements.*

- [ ] T021 Implement loading indicators in the UI for asynchronous data operations.
- [ ] T022 Implement user-friendly error handling for input validation and database errors.
- [ ] T023 Write unit tests for the ViewModels and the `LedgerRepository`.

---

## Dependencies

- **US1** is foundational.
- **US2** depends on **US1** (for the main screen).
- **US3** depends on **US1** (for the main screen).

## Implementation Strategy

The strategy is to build a fully functional, standalone Android application first. The foundational database layer (Phase 2) is the highest priority. After that, user stories can be implemented. FFI integration with the Rust backend can be added in a later version, ensuring the core Android app is stable and secure on its own.