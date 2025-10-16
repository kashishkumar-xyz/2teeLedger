# Tasks: Database Improvements for Querying and Data Integrity

**Input**: Design documents from `/specs/003-improvements-to-db/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: Tests are included for each user story as per the Test-First Development principle.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- No specific setup tasks required as the project structure is already established.

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Modify database schema and core `db` functions to support new `person` and `date` columns, and update related FFI and CLI layers.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

### Tests for Foundational Phase

**NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [X] T001 Write unit test for `initialize_db` in `tests/unit/db_test.rs` to verify new columns are created.
- [X] T002 Write unit test for `add_transaction` in `tests/unit/db_test.rs` to verify `person` and `date` are inserted into new columns.
- [X] T003 Write unit test for `list_transactions` in `tests/unit/db_test.rs` to verify it can retrieve `person` and `date` from new columns.

### Implementation for Foundational Phase

- [X] T004 Modify `initialize_db` to add `person` (TEXT) and `date` (TEXT) columns to `transactions_history` table in `ledger/src/db.rs`.
- [X] T005 Update `add_transaction` to accept `person` and `date` as direct arguments and insert them into the new columns in `ledger/src/db.rs`.
- [X] T006 Update `list_transactions` to query `person` and `date` from the new columns instead of `JSON_EXTRACT` in `ledger/src/db.rs`.
- [X] T007 Update `list_balances` to use the updated `list_transactions` in `ledger/src/db.rs`.
- [X] T008 Update `get_balance` to use the updated `list_transactions` in `ledger/src/db.rs`.
- [X] T009 Update `Transaction` struct in `ledger/src/models.rs` to include `person` and `date` fields for direct mapping from database columns.
- [X] T010 Update FFI functions (`add_transaction`, `list_transactions`, `list_balances`, `get_balance`) in `ledger/src/ffi.rs` to pass/receive `person` and `date` to/from the updated `db` functions.
- [X] T011 Update CLI commands (`add`, `list`, `balance`, `balances`) in `cli/src/main.rs` to pass/receive `person` and `date` to/from the updated FFI functions.

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Add and List Transactions (Priority: P1) 🎯 MVP

**Goal**: Ensure basic add and list functionality works with the new schema.

**Independent Test**: Add a transaction, then list all transactions and verify the output.

### Tests for User Story 1

**NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [X] T012 [US1] Write integration tests for `cli add` in `tests/integration/cli_test.rs`.
- [X] T013 [US1] Write integration tests for `cli list` (all) in `tests/integration/cli_test.rs`.

### Implementation for User Story 1

- (Implementation covered in Foundational Phase: T004-T011)

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - Filter Transactions by Person and Date (Priority: P1)

**Goal**: Ensure filtering by person and date works correctly.

**Independent Test**: Add transactions for multiple persons and dates, then list by person and by date, verifying filtered results.

### Tests for User Story 2

**NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [X] T014 [US2] Write unit tests for `list_transactions` (filter by person) in `tests/unit/db_test.rs`.
- [X] T015 [US2] Write unit tests for `list_transactions` (filter by date) in `tests/unit/db_test.rs`.
- [X] T016 [US2] Write unit tests for `list_transactions` (limit) in `tests/unit/db_test.rs`.
- [X] T017 [US2] Write integration tests for `cli list --person` in `tests/integration/cli_test.rs`.
- [X] T018 [US2] Write integration tests for `cli list --since` in `tests/integration/cli_test.rs`.
- [X] T019 [US2] Write integration tests for `cli list --limit` in `tests/integration/cli_test.rs`.

### Implementation for User Story 2

- (Implementation covered in Foundational Phase: T004-T011)

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently

---

## Phase 5: User Story 3 - View Balances (Priority: P1)

**Goal**: Ensure balance calculations are accurate.

**Independent Test**: Add transactions, then check all balances and individual balances, verifying accuracy.

### Tests for User Story 3

**NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [X] T020 [US3] Write unit tests for `list_balances` in `tests/unit/db_test.rs`.
- [X] T021 [US3] Write unit tests for `get_balance` in `tests/unit/db_test.rs`.
- [X] T022 [US3] Write integration tests for `cli balances` in `tests/integration/cli_test.rs`.
- [X] T023 [US3] Write integration tests for `cli balance --person` in `tests/integration/cli_test.rs`.

### Implementation for User Story 3

- (Implementation covered in Foundational Phase: T004-T011)

**Checkpoint**: All user stories should now be independently functional

---

## Phase 6: User Story 4 - Backup and Restore Database (Priority: P2)

**Goal**: Ensure backup and restore functionality works.

**Independent Test**: Add transactions, backup, restore to new path, verify contents.

### Tests for User Story 4

**NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [X] T024 [US4] Write unit tests for `backup_db` in `tests/unit/backup_test.rs`.
- [X] T025 [US4] Write unit tests for `restore_db` in `tests/unit/recovery_test.rs`.
- [X] T026 [US4] Write integration tests for `cli backup-db` in `tests/integration/cli_test.rs`.
- [X] T027 [US4] Write integration tests for `cli restore-db` in `tests/integration/cli_test.rs`.

### Implementation for User Story 4

- (No changes expected to `backup_db` or `restore_db` themselves, but they will operate on the new schema.)

---

## Phase 7: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [X] T028 Implement performance test for list operations (filtered by person/date) against 10,000 transactions benchmark in tests/performance/list_performance_test.rs
- [X] T029 Update `cli_commands_scenario.txt` in `tests/cli_commands_scenario.txt` to reflect the new schema and verify correct output.
- [X] T030 Run `cargo clippy` and address any warnings.
- [X] T031 Run `cargo fmt` to format the code.

---

## Dependencies & Execution Order

### Phase Dependencies

- **Foundational (Phase 2)**: Must complete before any User Story Phase. BLOCKS all user stories.
- **User Stories (Phase 3-6)**: All depend on Foundational phase completion. Can proceed in parallel after Foundational.
- **Polish (Phase 7)**: Depends on all desired user stories being complete.

### User Story Dependencies

- All user stories (US1, US2, US3, US4) are independent of each other after the Foundational phase.

### Within Each User Story

- Tests MUST be written and FAIL before implementation.
- Core implementation before integration.
- Story complete before moving to next priority.

### Parallel Opportunities

- Once Foundational phase completes, all user stories (US1, US2, US3, US4) can start in parallel (if team capacity allows).
- Within each user story, test tasks can be run in parallel.

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 2: Foundational (CRITICAL - blocks all stories)
2. Complete Phase 3: User Story 1
3. **STOP and VALIDATE**: Test User Story 1 independently
4. Deploy/demo if ready

### Incremental Delivery

1. Complete Foundational Phase → Foundation ready
2. Add User Story 1 → Test independently → Deploy/Demo (MVP!)
3. Add User Story 2 → Test independently → Deploy/Demo
4. Add User Story 3 → Test independently → Deploy/Demo
5. Add User Story 4 → Test independently → Deploy/Demo
6. Each story adds value without breaking previous stories

### Parallel Team Strategy

With multiple developers:

1. Team completes Foundational Phase together
2. Once Foundational is done:
    - Developer A: User Story 1
    - Developer B: User Story 2
    - Developer C: User Story 3
    - Developer D: User Story 4
3. Stories complete and integrate independently

---

## Notes

- Each user story should be independently completable and testable
- Verify tests fail before implementing
- Commit after each task or logical group
- Stop at any checkpoint to validate story independently
- Avoid: vague tasks, same file conflicts, cross-story dependencies that break independence

