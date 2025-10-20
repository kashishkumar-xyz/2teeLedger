# Data Model: Keypad UI

**Date**: 2025-10-21

This document outlines the data model for the Keypad UI feature. As this is a UI-only feature, the data model represents the transient state of the UI, managed within a ViewModel.

## Entities

### KeypadState

Represents the complete state of the keypad screen at any given moment.

**Fields**:

- `displayedValue: String`
  - **Description**: The numerical string currently displayed on the screen. This value is built from the user's input.
  - **Validation**: Can contain digits (0-9) and at most one decimal point.
  - **Initial State**: "0"

- `theme: KeypadTheme`
  - **Description**: An enumeration representing the current visual theme of the keypad.
  - **Values**: `GREEN`, `RED`
  - **Initial State**: `GREEN` (or as configured on launch)

## State Transitions

The `KeypadState` is updated by the `KeypadViewModel` in response to user actions:

- **User taps a number button**: The corresponding digit is appended to `displayedValue`.
- **User taps the decimal button**: A `.` is appended to `displayedValue` if it does not already contain one.
- **User taps the backspace button**: The last character is removed from `displayedValue`.
- **User long-presses the backspace button**: `displayedValue` is reset to "0".
- **Theme is toggled**: The `theme` value is updated, causing the UI to recompose with the new color scheme.
