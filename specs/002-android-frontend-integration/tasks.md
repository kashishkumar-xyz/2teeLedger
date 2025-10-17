# Tasks: Android UI for teeLedger

**Input**: Design documents from `/specs/002-android-frontend-integration/`

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [X] T001 Create the Android project structure in the `app/` directory.
- [X] T002 Configure `app/build.gradle.kts` to execute a `cargo-ndk` build, compiling the Rust core library for all target Android ABIs.

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

- [X] T003 Implement the JNI bridge in `app/src/main/java/com/example/teeledger/jni/LedgerCore.kt` to connect to the Rust FFI functions.
- [X] T004 Implement secure key management to retrieve the encryption key from the Android Keystore and pass it to the JNI bridge.
- [X] T005 [P] Create the Kotlin data class `BalanceSummary` in `app/src/main/java/com/example/teeledger/models/BalanceSummary.kt`.
- [X] T006 [P] Create the Kotlin data class `TransactionDetails` in `app/src/main/java/com/example/teeledger/models/TransactionDetails.kt`.

**Checkpoint**: Foundation ready - user story implementation can now begin.

---

## Phase 3: User Story 1 - View Balances (Priority: P1) 🎯 MVP

**Goal**: As a user, I want to open the app and immediately see a summary of how much each person owes me or I owe them.

**Independent Test**: A user can open the application and see a list of all people with their current aggregated balances.

### Tests for User Story 1
- [X] T007 [US1] Write a failing unit test for `LedgerViewModel` in `app/src/test/java/com/example/teeledger/viewmodel/LedgerViewModelTest.kt` that verifies the correct fetching of balances.

### Implementation for User Story 1

- [X] T008 [US1] Implement `LedgerViewModel` in `app/src/main/java/com/example/teeledger/viewmodel/LedgerViewModel.kt` to fetch balances via the JNI bridge.
- [X] T009 [US1] Create the UI for the main balance screen in `app/src/main/res/layout/activity_main.xml`.
- [X] T010 [US1] Implement `MainActivity.kt` in `app/src/main/java/com/example/teeledger/MainActivity.kt` to display the list of balances using the ViewModel.

**Checkpoint**: User Story 1 should be fully functional and testable independently.

---

## Phase 4: User Story 2 - Add a New Transaction (Priority: P1)

**Goal**: As a user, I want to easily add a new financial transaction from the main screen.

**Independent Test**: A user can navigate from the main screen, fill out a form, and successfully add a new transaction, which then updates the balances on the main screen.

### Tests for User Story 2
- [X] T011 [US2] Write a failing unit test for `AddTransactionViewModel` in `app/src/test/java/com/example/teeledger/viewmodel/AddTransactionViewModelTest.kt` covering input validation and transaction submission.

### Implementation for User Story 2

- [X] T012 [US2] Implement input validation logic within `AddTransactionViewModel` as per `spec.md:FR-004`.
- [X] T013 [US2] Implement `AddTransactionViewModel` in `app/src/main/java/com/example/teeledger/viewmodel/AddTransactionViewModel.kt`.
- [X] T014 [US2] Create the UI for the "add transaction" screen in `app/src/main/res/layout/activity_add_transaction.xml`.
- [X] T015 [US2] Implement `AddTransactionActivity.kt` in `app/src/main/java/com/example/teeledger/AddTransactionActivity.kt` to handle user input and call the ViewModel.
- [X] T016 [US2] Add navigation from `MainActivity` to `AddTransactionActivity`.

**Checkpoint**: User Stories 1 AND 2 should both work.

---

## Phase 5: User Story 3 - View Transaction History (Priority: P2)

**Goal**: As a user, I want to tap on a person's name in the balance list to see a detailed history of all our transactions.

**Independent Test**: A user can select a person from the main screen and view a new screen listing all transactions associated with that person.

### Tests for User Story 3
- [X] T017 [US3] Write a failing unit test for `TransactionHistoryViewModel` in `app/src/test/java/com/example/teeledger/viewmodel/TransactionHistoryViewModelTest.kt`.

### Implementation for User Story 3

- [X] T018 [US3] Implement `TransactionHistoryViewModel` in `app/src/main/java/com/example/teeledger/viewmodel/TransactionHistoryViewModel.kt`.
- [X] T019 [US3] Create the UI for the transaction history screen in `app/src/main/res/layout/activity_transaction_history.xml`.
- [X] T020 [US3] Implement `TransactionHistoryActivity.kt` in `app/src/main/java/com/example/teeledger/TransactionHistoryActivity.kt` to display the transaction list.
- [X] T021 [US3] Add navigation from `MainActivity` to `TransactionHistoryActivity`.

**Checkpoint**: All user stories should now be functional.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories.

- [X] T022 [P] Implement loading indicators for all screens that perform background operations.
- [X] T023 [P] Implement UI to display errors from the FFI layer. The UI should show a user-friendly message with an option to view the detailed technical error message, per `spec.md:FR-007`.
- [X] T024 [P] Add Espresso UI tests for the main user flows (Add Transaction, View History).
- [X] T025 [P] Add performance tests to measure application launch time and ensure it meets `spec.md:NFR-002`.

---

## Dependencies & Execution Order

- **Setup (Phase 1)** and **Foundational (Phase 2)** must be completed before any user stories.
- Within each user story phase, tests must be written and fail before implementation begins.
- User stories can be implemented in priority order (US1, US2, US3).

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1 & 2.
2. Complete Phase 3 (User Story 1), following the test-first approach.
3. **STOP and VALIDATE**: Test User Story 1 independently.
