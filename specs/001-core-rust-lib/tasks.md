# Tasks: Core Rust Library Development

**Input**: Design documents from `/specs/001-core-rust-lib/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/, quickstart.md

**Tests**: The feature specification explicitly requests comprehensive unit tests.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- **Single project**: `src/`, `tests/` at repository root
- Paths shown below assume single project - adjust based on plan.md structure

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [x] T001 Create Rust library project (`cdylib`) in `src/`
- [x] T002 Configure `Cargo.toml` with `rusqlite`, `jni`, `zeroize`, `base64`, `hmac`, `clap` dependencies
- [x] T003 [P] Create `src/models.rs`, `src/db.rs`, `src/ffi.rs`, `src/key_management.rs`, `src/backup.rs`, `src/recovery.rs`
- [x] T004 [P] Create `cli/src/main.rs` for the CLI application
- [x] T005 [P] Create `data/backups/` directory
- [x] T006 [P] Configure `tests/unit/` and `tests/integration/` directories with initial test files
- [x] T007 Implement HMAC-SHA256 for backup checksums in `src/backup.rs`
- [x] T008 Implement CLI argument parsing using `clap` in `cli/src/main.rs`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [x] T009 Implement SQLCipher integration in `db.rs` (ensure `rusqlite` is compiled with SQLCipher feature)
- [x] T010 Implement DEK generation and Android Keystore wrapping/unwrapping in `key_management.rs`
- [x] T011 Implement `PRAGMA journal_mode = WAL;` and `PRAGMA integrity_check;` logic in `db.rs`
- [x] T012 Implement `open_encrypted_db` function in `db.rs`
- [x] T013 Implement `backup_db` FFI function in `backup.rs` and expose via `ffi.rs`
- [x] T014 Implement `restore_db` FFI function in `recovery.rs` and expose via `ffi.rs`
- [x] T015 Implement append-only `transactions_history` table schema in `db.rs` (migration logic)
- [x] T016 Implement zeroization of sensitive keys in `key_management.rs` and `db.rs`

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Record a new transaction (Priority: P1) 🎯 MVP

**Goal**: Allow users to record new financial transactions.

**Independent Test**: A user can successfully add a transaction and see it reflected in the transaction list.

### Tests for User Story 1 ⚠️

**NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [x] T017 [P] [US1] Unit test for `Transaction` struct validation in `tests/unit/models_test.rs`
- [x] T018 [P] [US1] Unit test for `add_transaction` logic (including input validation and history recording) in `tests/unit/db_test.rs`
- [x] T019 [P] [US1] Unit test for `add_transaction` FFI exposure in `tests/unit/ffi_test.rs`
- [] T020 [P] [US1] CLI integration test for `add` command in `tests/integration/cli_test.rs`

### Implementation for User Story 1

- [x] T021 [P] [US1] Define `Transaction` struct in `src/models.rs`
- [x] T022 [US1] Implement `add_transaction` logic in `src/db.rs` (including input validation and writing to `transactions_history`)
- [x] T023 [US1] Expose `add_transaction` via FFI in `src/ffi.rs`
- [] T024 [US1] Implement `add` command in `cli/src/main.rs` to call `add_transaction` FFI

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - View all transactions (Priority: P1)

**Goal**: Allow users to view a list of all recorded transactions.

**Independent Test**: A user can open the application and see a list of all previously recorded transactions.

### Tests for User Story 2 ⚠️

**NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [] T025 [P] [US2] Unit test for `list_transactions` logic (reconstructing state from history) in `tests/unit/db_test.rs`
- [] T026 [P] [US2] Unit test for `list_transactions` FFI exposure in `tests/unit/ffi_test.rs`
- [] T027 [P] [US2] CLI integration test for `list` command in `tests/integration/cli_test.rs`

### Implementation for User Story 2

- [] T028 [US2] Implement `list_transactions` logic in `src/db.rs`
- [] T029 [US2] Expose `list_transactions` via FFI in `src/ffi.rs`
- [] T030 [US2] Implement `list` command in `cli/src/main.rs` to call `list_transactions` FFI

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently

---

## Phase 5: User Story 3 - View balances for all people (Priority: P1)

**Goal**: Allow users to see a summary of how much each person owes or is owed.

**Independent Test**: A user can see a list of all people with their current aggregated balances.

### Tests for User Story 3 ⚠️

**NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [ ] T031 [P] [US3] Unit test for `Balance` struct in `tests/unit/models_test.rs`
- [ ] T032 [P] [US3] Unit test for `list_balances` logic (aggregating from history) in `tests/unit/db_test.rs`
- [ ] T033 [P] [US3] Unit test for `list_balances` FFI exposure in `tests/unit/ffi_test.rs`
- [ ] T034 [P] [US3] CLI integration test for `balances` command in `tests/integration/cli_test.rs`

### Implementation for User Story 3

- [ ] T035 [P] [US3] Define `Balance` struct in `src/models.rs`
- [ ] T036 [US3] Implement `list_balances` logic in `src/db.rs`
- [ ] T037 [US3] Expose `list_balances` via FFI in `src/ffi.rs`
- [ ] T038 [US3] Implement `balances` command in `cli/src/main.rs` to call `list_balances` FFI

**Checkpoint**: All user stories should now be independently functional

---

## Phase 6: User Story 4 - View individual person's balance (Priority: P2)

**Goal**: Allow users to view the total amount a specific person owes or is owed.

**Independent Test**: A user can select a person and see their individual balance.

### Tests for User Story 4 ⚠️

**NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [ ] T039 [P] [US4] Unit test for `get_balance` logic in `tests/unit/models_test.rs`
- [ ] T040 [P] [US4] Unit test for `get_balance` FFI exposure in `tests/unit/ffi_test.rs`
- [ ] T041 [P] [US4] CLI integration test for `balance <person>` command in `tests/integration/cli_test.rs`

### Implementation for User Story 4

- [ ] T042 [US4] Implement `get_balance` logic in `src/db.rs`
- [ ] T043 [US4] Expose `get_balance` via FFI in `src/ffi.rs`
- [ ] T044 [US4] Implement `balance <person>` command in `cli/src/main.rs` to call `get_balance` FFI

**Checkpoint**: All user stories should now be independently functional

---

## Phase 7: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [ ] T045 Implement automated corruption detection and recovery flow in `src/recovery.rs`
- [ ] T046 Implement backup metadata with HMAC and backup rotation policy in `src/backup.rs`
- [ ] T047 Refine error handling and logging across `src/db.rs`, `src/key_management.rs`, `src/backup.rs`, `src/recovery.rs`, `cli/src/main.rs`
- [ ] T048 Update `docs/README.md`, `docs/spec-sheet.md`, `docs/ANDROID_INTEGRATION.md` with new features and guidelines
- [ ] T049 Write integration tests for full ledger flow, including backup/restore and corruption scenarios in `tests/integration/ledger_integration_test.rs`
- [ ] T050 Code cleanup and refactoring
- [ ] T051 Document CLI usage and commands in `docs/CLI_USAGE.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3+)**: All depend on Foundational phase completion
  - User stories can then proceed in parallel (if staffed)
  - Or sequentially in priority order (P1 → P2 → P3)
- **Polish (Final Phase)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational (Phase 2) - No dependencies on other stories
- **User Story 2 (P1)**: Can start after Foundational (Phase 2) - May integrate with US1 but should be independently testable
- **User Story 3 (P1)**: Can start after Foundational (Phase 2) - May integrate with US1/US2 but should be independently testable
- **User Story 4 (P2)**: Can start after Foundational (Phase 2) - May integrate with US1/US2/US3 but should be independently testable

### Within Each User Story

- Tests (if included) MUST be written and FAIL before implementation
- Models before services
- Services before endpoints
- Core implementation before integration
- Story complete before moving to next priority

### Parallel Opportunities

- All Setup tasks marked [P] can run in parallel
- All Foundational tasks marked [P] can run in parallel (within Phase 2)
- Once Foundational phase completes, all user stories can start in parallel (if team capacity allows)
- All tests for a user story marked [P] can run in parallel
- Models within a story marked [P] can run in parallel
- Different user stories can be worked on in parallel by different team members

---

## Parallel Example: User Story 1

```bash
# Launch all tests for User Story 1 together:
Task: "Unit test for Transaction struct validation in tests/unit/models_test.rs"
Task: "Unit test for add_transaction logic (including input validation and history recording) in tests/unit/db_test.rs"
Task: "Unit test for add_transaction FFI exposure in tests/unit/ffi_test.rs"
Task: "CLI integration test for add command in tests/integration/cli_test.rs"

# Launch all models for User Story 1 together:
Task: "Define Transaction struct in src/models.rs"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational (CRITICAL - blocks all stories)
3. Complete Phase 3: User Story 1
4. **STOP and VALIDATE**: Test User Story 1 independently
5. Deploy/demo if ready

### Incremental Delivery

1. Complete Setup + Foundational → Foundation ready
2. Add User Story 1 → Test independently → Deploy/Demo (MVP!)
3. Add User Story 2 → Test independently → Deploy/Demo
4. Add User Story 3 → Test independently → Deploy/Demo
5. Add User Story 4 → Test independently → Deploy/Demo
6. Each story adds value without breaking previous stories

### Parallel Team Strategy

With multiple developers:

1. Team completes Setup + Foundational together
2. Once Foundational is done:
   - Developer A: User Story 1
   - Developer B: User Story 2
   - Developer C: User Story 3
   - Developer D: User Story 4
3. Stories complete and integrate independently

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to specific user story for traceability
- Each user story should be independently completable and testable
- Verify tests fail before implementing
- Commit after each task or logical group
- Stop at any checkpoint to validate story independently
- Avoid: vague tasks, same file conflicts, cross-story dependencies that break independence
