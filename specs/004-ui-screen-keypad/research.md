# Research: Keypad UI Screen

**Branch**: `004-ui-screen-keypad` | **Date**: 2025-10-21 | **Spec**: [./spec.md](./spec.md)

## Research Tasks

This research phase focuses on gathering the necessary information to build a responsive, scalable, and maintainable Keypad UI screen in Jetpack Compose.

### 1. State Management in Jetpack Compose

**Task**: Investigate best practices for state management in Jetpack Compose, specifically for a feature like a keypad where the UI state is simple but needs to be handled efficiently.

**Findings**:

- **Decision**: Use a `ViewModel` to hold the state of the keypad screen. The state will be exposed to the UI using `StateFlow` or `MutableState`.
- **Rationale**: A `ViewModel` is the standard Android-recommended way to handle UI-related data that survives configuration changes. Using `StateFlow` allows the UI to reactively observe state changes and recompose efficiently. This approach separates the state logic from the UI, making the code cleaner and easier to test.
- **Alternatives considered**:
    - Using `remember` and `mutableStateOf` directly in the Composable: This is suitable for simple, internal state of a Composable, but for screen-level state that needs to be preserved, a `ViewModel` is better.
    - Using a third-party state management library: Overkill for the simple state of the keypad screen.

### 2. Responsive Layouts and Adaptive Sizing

**Task**: Research techniques for creating responsive layouts and adaptive component sizing in Jetpack Compose to ensure the keypad looks good on different screen sizes and orientations.

**Findings**:

- **Decision**:
    - Use `BoxWithConstraints` to get the available screen space and make decisions about the layout.
    - Use `Modifier.weight()` within `Row` and `Column` to create flexible layouts where components share space proportionally.
    - Use `Layout` composable for more complex custom layouts if needed.
    - Define dimensions in `dp` but also consider using `dimens.xml` for different screen sizes if necessary.
- **Rationale**: These are the standard Jetpack Compose APIs for building adaptive UIs. `BoxWithConstraints` provides the flexibility to adapt the layout based on the available space, while `weight` is perfect for distributing space among the keypad buttons.
- **Alternatives considered**:
    - Creating different layouts for different screen sizes: This is a more traditional approach but less flexible than using Compose's adaptive APIs.

### 3. Review of `KeypadScreen.kt`

**Task**: Analyze the provided `KeypadScreen.kt` to understand the UI components, styling, and overall visual structure.

**Findings**:

- The file provides a good visual representation of the keypad.
- It uses `MaterialTheme` and custom colors.
- The layout is static and does not adapt to different screen sizes.
- The buttons are hardcoded.
- State management is not implemented.

### 4. Review of `kbscreen.bk`

**Task**: Examine the `kbscreen.bk` file to extract the core logic for input handling and dynamic layout generation.

**Findings**:

- The file contains the logic for handling user input (number presses, backspace, etc.).
- It has a dynamic layout that can be adapted.
- The state management is basic and can be improved with a `ViewModel`.

## Conclusion

The research confirms that combining the UI from `KeypadScreen.kt` with the logic from `kbscreen.bk` is feasible. The new implementation will use a `ViewModel` for state management and Jetpack Compose's adaptive layout features to create a responsive and scalable keypad screen.