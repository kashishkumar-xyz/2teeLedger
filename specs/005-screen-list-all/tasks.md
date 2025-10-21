# Tasks: Search Screen for Account Balances

**Input**: Design documents from `/specs/005-screen-list-all/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: The examples below include test tasks. Tests are OPTIONAL - only include them if explicitly requested in the feature specification.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`
- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions
- **Single project**: `src/`, `tests/` at repository root
- **Web app**: `backend/src/`, `frontend/src/`
- **Mobile**: `api/src/`, `ios/src/` or `android/src/`
- Paths shown below assume single project - adjust based on plan.md structure

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [ ] T001 Create Android project structure for `ui/search`, `data`, `ffi` in `android/app/src/main/kotlin/com/example/app/`
- [ ] T002 Configure Android project for Jetpack Compose in `android/app/build.gradle.kts`
- [ ] T003 Ensure Rust FFI bindings are correctly generated and linked in `android/app/build.gradle.kts` and `ledger/build.rs`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [ ] T004 Implement FFI interface in Android: Create `FfiBindings.kt` in `android/app/src/main/kotlin/com/example/app/ffi/`
- [ ] T005 Implement `AccountRepository.kt` to use FFI for data retrieval in `android/app/src/main/kotlin/com/example/app/data/`

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Search for an existing account by name (Priority: P1) 🎯 MVP

**Goal**: Allow users to search for account holders by name and view their balances.

**Independent Test**: Open the Search Screen, type a partial name, and verify that matching accounts are displayed with correct balance information and styling.

### Tests for User Story 1

- [ ] T006 [US1] Write unit tests for `SearchViewModel.kt` in `android/app/src/test/kotlin/com/example/app/ui/search/SearchViewModelTest.kt`
- [ ] T007 [US1] Write UI tests for `SearchScreen.kt` (initial load, keyboard visible) in `android/app/src/androidTest/kotlin/com/example/app/ui/search/SearchScreenTest.kt`

### Implementation for User Story 1

- [ ] T008 [US1] Implement `SearchScreen.kt` (UI layout, search bar, account list) in `android/app/src/main/kotlin/com/example/app/ui/search/SearchScreen.kt`
- [ ] T009 [US1] Implement `SearchViewModel.kt` (handle search logic, interact with `AccountRepository`) in `android/app/src/main/kotlin/com/example/app/ui/search/SearchViewModel.kt`
- [ ] T010 [US1] Apply dark mode theme and specified colors (FR-007) in `android/app/src/main/res/values/colors.xml` and `android/app/src/main/kotlin/com/example/app/ui/theme/Theme.kt`
- [ ] T011 [US1] Apply specified typography (FR-008) in `android/app/src/main/kotlin/com/example/app/ui/theme/Type.kt`
- [ ] T012 [US1] Implement balance color-coding (green for positive, red for negative) in `SearchScreen.kt` (FR-006)
- [ ] T013 [US1] Ensure native Android keyboard is visible and active on screen load (FR-002) in `SearchScreen.kt`
- [ ] T014 [US1] Write UI tests for `SearchScreen.kt` (search functionality, balance color-coding) in `android/app/src/androidTest/kotlin/com/example/app/ui/search/SearchScreenTest.kt`

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - No matching accounts found (Priority: P2)

**Goal**: Inform the user when a search yields no results.

**Independent Test**: Type a non-existent name into the search bar and verify that a "No results found" message is displayed.

### Tests for User Story 2

- [ ] T015 [US2] Write UI tests for "No results found" scenario in `android/app/src/androidTest/kotlin/com/example/app/ui/search/SearchScreenTest.kt`

### Implementation for User Story 2

- [ ] T016 [US2] Implement "No results found" message in `SearchScreen.kt` (FR-009)

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently

---

## Phase 5: User Story 3 - Initiate adding a new contact (Priority: P3)

**Goal**: Provide an entry point for managing contacts directly from the search screen.

**Independent Test**: Tap the "+ New Contact" button and verify that the application navigates to the designated contact creation screen.

### Tests for User Story 3

- [ ] T017 [US3] Write UI tests for "+ New Contact" button in `android/app/src/androidTest/kotlin/com/example/app/ui/search/SearchScreenTest.kt`

### Implementation for User Story 3

- [ ] T018 [US3] Implement "+ New Contact" button in `SearchScreen.kt` (FR-003)
- [ ] T019 [US3] Implement navigation to "Add New Contact" screen from `SearchScreen.kt`

**Checkpoint**: All user stories should now be independently functional

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [ ] T020 Implement edge case: Empty search query (display "Suggestions" title and "+ New Contact" button, no account list) in `SearchScreen.kt`
- [ ] T021 Implement edge case: Backend Service Unavailable (display error message) in `SearchScreen.kt` and `SearchViewModel.kt`
- [ ] T022 Implement edge case: Missing Avatar (display default placeholder) in `SearchScreen.kt` (FR-010)
- [ ] T023 Implement edge case: Long Account Holder Name (truncate with ellipsis or wrap text) in `SearchScreen.kt`
- [ ] T024 Review and refine UI/UX for overall polish and adherence to visual reference (`docs/UI-docs/designs/screens/account-search/ui.png`).
- [ ] T025 Performance testing and optimization for search queries and UI rendering.
- [ ] T026 Accessibility checks for the Search Screen.

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
- **User Story 2 (P2)**: Can start after Foundational (Phase 2) - May integrate with US1 but should be independently testable
- **User Story 3 (P3)**: Can start after Foundational (Phase 2) - May integrate with US1/US2 but should be independently testable

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
# Launch all tests for User Story 1 together (if tests requested):
Task: "T006 [US1] Write unit tests for `SearchViewModel.kt` in `android/app/src/test/kotlin/com/example/app/ui/search/SearchViewModelTest.kt`"
Task: "T007 [US1] Write UI tests for `SearchScreen.kt` (initial load, keyboard visible) in `android/app/src/androidTest/kotlin/com/example/app/ui/search/SearchScreenTest.kt`"

# Launch all models for User Story 1 together:
Task: "T008 [US1] Implement `SearchScreen.kt` (UI layout, search bar, account list) in `android/app/src/main/kotlin/com/example/app/ui/search/SearchScreen.kt`"
Task: "T009 [US1] Implement `SearchViewModel.kt` (handle search logic, interact with `AccountRepository`) in `android/app/src/main/kotlin/com/example/app/ui/search/SearchViewModel.kt`"
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
5. Each story adds value without breaking previous stories

### Parallel Team Strategy

With multiple developers:

1. Team completes Setup + Foundational together
2. Once Foundational is done:
   - Developer A: User Story 1
   - Developer B: User Story 2
   - Developer C: User Story 3
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
