# Tasks: Keypad UI Screen

**Input**: Design documents from `/specs/004-ui-screen-keypad/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/

**Tests**: Tests are included as per the Test-First Development principle in the constitution.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`
- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2)
- Include exact file paths in descriptions

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [x] T001 Create directory `android/app/src/main/java/com/example/ledger/ui/keypad/theme/`
- [x] T002 Create directory `android/app/androidTest/java/com/example/ledger/ui/keypad/`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

- [x] T003 [P] Create `android/app/src/main/java/com/example/ledger/ui/keypad/theme/Color.kt` and define the green and red color palettes.
- [x] T004 [P] Create `android/app/src/main/java/com/example/ledger/ui/keypad/theme/Theme.kt` and implement the `KeypadTheme` composable wrapper around `MaterialTheme`.
- [x] T005 Create `android/app/src/main/java/com/example/ledger/ui/keypad/KeypadViewModel.kt` with the `KeypadState` data class and `KeypadTheme` enum.

**Checkpoint**: Foundation ready - user story implementation can now begin.

---

## Phase 3: User Story 1 - Basic Input (Priority: P1) 🎯 MVP

**Goal**: Allow users to tap numbers on a keypad and see them on a display.

**Independent Test**: Launch the screen, tap number and backspace buttons, and verify the display updates correctly.

### Tests for User Story 1 ⚠️

**NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [x] T006 [US1] Create `android/app/androidTest/java/com/example/ledger/ui/keypad/KeypadScreenTest.kt`.
- [x] T007 [US1] In `KeypadScreenTest.kt`, write a test to verify that tapping number buttons updates the display.
- [x] T008 [US1] In `KeypadScreenTest.kt`, write a test to verify that a short press on the backspace button removes the last digit.
- [x] T009 [US1] In `KeypadScreenTest.kt`, write a test to verify that a long press on the backspace button clears the display.

### Implementation for User Story 1

- [x] T010 [US1] In `KeypadViewModel.kt`, implement the logic to handle number input and update `displayedValue` in the `KeypadState`.
- [x] T011 [US1] In `KeypadViewModel.kt`, implement the logic for single-digit backspace and clearing the display.
- [x] T012 [US1] Create `android/app/src/main/java/com/example/ledger/ui/keypad/KeypadScreen.kt` and implement the basic UI layout with a display area and keypad buttons.
- [x] T013 [US1] In `KeypadScreen.kt`, connect the UI to the `KeypadViewModel` to display the `displayedValue` and handle button clicks.

**Checkpoint**: User Story 1 should be fully functional and testable independently.

---

## Phase 4: User Story 2 - Themed Visual Feedback (Priority: P2)

**Goal**: Provide clear visual context for transactions with green (income) and red (expense) themes.

**Independent Test**: Launch the screen with each theme and verify the colors match the design mockups.

### Tests for User Story 2 ⚠️

- [x] T014 [P] [US2] In `KeypadScreenTest.kt`, write a test to verify the UI uses the green color scheme when the theme is set to `GREEN`.
- [x] T015 [P] [US2] In `KeypadScreenTest.kt`, write a test to verify the UI uses the red color scheme when the theme is set to `RED`.

### Implementation for User Story 2

- [x] T016 [US2] In `KeypadViewModel.kt`, implement a method to allow setting the `KeypadTheme` in the `KeypadState`.
- [x] T017 [US2] In `KeypadScreen.kt`, apply the `KeypadTheme` from the `KeypadState` to the UI elements.

**Checkpoint**: User Stories 1 AND 2 should both work independently.

---

## Phase 5: Polish & Cross-Cutting Concerns

**Purpose**: Final integration, cleanup, and validation.

- [x] T018 Modify `android/app/src/main/java/com/example/ledger/ui/MainActivity.kt` to launch the `KeypadScreen` as per the `quickstart.md` for manual testing.
- [x] T019 Run all tests and ensure they pass.
- [x] T020 Perform a final code review and refactor for clarity and performance.

---

## Dependencies & Execution Order

- **Setup (Phase 1)** -> **Foundational (Phase 2)** -> **User Stories (Phase 3 & 4)** -> **Polish (Phase 5)**
- User Story 1 and User Story 2 can be developed sequentially. US1 is the MVP.

## Implementation Strategy

1.  Complete Phase 1 & 2 to set up the foundation.
2.  Implement Phase 3 (User Story 1) to deliver the core MVP.
3.  Implement Phase 4 (User Story 2) to add theming.
4.  Complete Phase 5 for final integration and polish.
