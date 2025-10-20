# Data Model: Keypad UI Screen

**Branch**: `004-ui-screen-keypad` | **Date**: 2025-10-21 | **Spec**: [./spec.md](./spec.md)

This document describes the data entities for the Keypad UI screen feature. As this is a UI-only feature with transient state, the data model is simple and does not involve any persistent storage.

## Key Entities

### 1. `KeypadState`

Represents the complete state of the keypad screen at any given moment. This state is managed by the `KeypadViewModel` and observed by the `KeypadScreen` composable.

**Fields**:

- `displayText: String`
  - **Description**: The current string of numbers displayed on the screen. This is the value being built by the user's input.
  - **Type**: `String`
  - **Initial Value**: `"0"`
  - **Constraints**: Can only contain numeric characters and at most one decimal point.

- `theme: KeypadTheme`
  - **Description**: An enum or sealed class that defines the current visual theme of the keypad.
  - **Type**: `KeypadTheme` (e.g., `GREEN`, `RED`)
  - **Initial Value**: `GREEN` (or as configured on launch)

**State Transitions**:

- **Append Digit**: When a number button is pressed, the corresponding digit is appended to `displayText`.
- **Delete Digit**: When the backspace button is pressed, the last character of `displayText` is removed.
- **Clear Display**: When the clear button is long-pressed, `displayText` is reset to `"0"`.
- **Toggle Theme**: The theme can be changed based on the context of the transaction (e.g., income vs. expense).