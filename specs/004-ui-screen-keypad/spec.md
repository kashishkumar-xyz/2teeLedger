# Feature Specification: Keypad UI Screen

**Feature Branch**: `004-ui-screen-keypad`  
**Created**: 2025-10-21
**Status**: Draft  
**Input**: User description: "ui_screen:keypad explanation of what the screen should look like: @docs/UI-docs/UI_layout.md pictures of what i expect final output to look like: @docs/UI-docs/designs/screens/keypad_green/gr.png @docs/UI-docs/designs/screens/keypad_red/re.png HTML designs that will act as guiding path for our android implementation of them: @docs/UI-docs/designs/screens/keypad_green/gr.html @docs/UI-docs/designs/screens/keypad_red/re.html pre-existing android frontend where this new screen will be created is in the following directory: @android/app/src/main/java/com/example/ledger/ui/ this new screen we are specifying will replace the following screen in our new UI: @android/app/src/main/java/com/example/ledger/ui/AddTransactionScreen.kt for now lets make it a dummy UI, where we just implement the graphical UI, it shouldnt try to fetch or interact with our rust backend just yet. the only data the UI should handle is the one it receives directly from user interaction such as the keypad inputs etc. To ensure we dont break any pre-existing code, lets build the new keypad screen completely independant of the existing screens, we will just edit the entry point of our app to point to this new screen for testing the UI"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Basic Input (Priority: P1)

As a user, I want to be able to tap numbers on a keypad and see them appear on a display, so that I can enter numerical values for transactions.

**Why this priority**: This is the core functionality of the keypad screen. Without it, the user cannot input any data.

**Independent Test**: The screen can be launched, and a tester can tap number buttons to verify that the correct digits appear in the display area. This confirms the primary input mechanism works as expected.

**Acceptance Scenarios**:

1. **Given** the keypad screen is displayed and the input display is empty (or "0"), **When** the user taps the "7" button, **Then** the display shows "7".
2. **Given** the display currently shows "7", **When** the user taps the "5" button, **Then** the display shows "75".
3. **Given** the keypad screen is displayed, **When** the user taps a sequence of numbers (e.g., "1", "2", "3"), **Then** the display correctly shows "123".

---

### User Story 2 - Themed Visual Feedback (Priority: P2)

As a user, I want the keypad to appear in different color schemes (green or red) to provide clear visual context for the type of transaction I am entering (e.g., green for income, red for an expense).

**Why this priority**: This provides immediate visual feedback to the user, reducing errors and improving the user experience by reinforcing the transaction's nature.

**Independent Test**: The screen can be launched with a specific theme (e.g., "red"). A tester can verify that all relevant UI elements (buttons, background) match the "red" design mockup. The same test can be repeated for the "green" theme.

**Acceptance Scenarios**:

1. **Given** the screen is configured for an "income" transaction, **When** the keypad is displayed, **Then** it renders using the "green" color scheme as defined in `gr.png` and `gr.html`.
2. **Given** the screen is configured for an "expense" transaction, **When** the keypad is displayed, **Then** it renders using the "red" color scheme as defined in `re.png` and `re.html`.

---

### Edge Cases

- What happens when the user enters a number longer than the display can show? The display should scroll horizontally to accommodate the number.
- What happens if the user tries to enter more than one decimal point? The keypad should prevent the entry of a second decimal point.
- How does the UI respond to screen rotation? The screen should be locked to portrait orientation to prevent rotation.
- What is the behavior of the backspace/clear button? A short press should delete the last digit, while a long press (500ms) should clear the entire display.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The system MUST display a numerical keypad for user input.
- **FR-002**: The system MUST include a display area to visually represent the numerical value entered by the user.
- **FR-003**: When a user taps a number button, the system MUST append that number to the current value in the display.
- **FR-004**: The UI MUST be capable of rendering in two distinct visual themes: a "green" theme and a "red" theme.
- **FR-005**: The overall layout of the screen MUST conform to the structure specified in `@docs/UI-docs/UI_layout.md`.
- **FR-006**: The visual styling of the "green" and "red" themes MUST match the respective mockups (`gr.png`, `re.png`) and HTML designs (`gr.html`, `re.html`).
- **FR-007**: For initial development and testing, the application's entry point MUST be modified to launch the keypad screen directly.
- **FR-008**: The keypad screen MUST be implemented as a standalone "dummy" UI, with no dependencies on or interactions with the Rust backend or any database.
- **FR-009**: The new keypad screen is intended to eventually replace the existing screen at `@android/app/src/main/java/com/example/ledger/ui/AddTransactionScreen.kt`.

### Key Entities *(include if feature involves data)*

- **UserInput**: Represents the sequence of characters entered by the user via the keypad. This is transient data that exists only for the duration the screen is active.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: The keypad screen MUST render successfully on a target Android device or emulator, displaying all UI elements as specified in the designs.
- **SC-002**: User input latency MUST be less than 200ms from the moment a key is tapped to the moment the display is updated.
- **SC-003**: The "green" and "red" themes MUST be visually identical to the provided design mockups (`gr.png`, `re.png`) when inspected on a target device.
- **SC-004**: The feature MUST be implemented in complete isolation, ensuring that running all pre-existing automated tests results in a 100% pass rate, confirming no regressions have been introduced.