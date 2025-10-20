# Research: Keypad UI Screen

**Date**: 2025-10-21

This document summarizes the research conducted to resolve the `NEEDS CLARIFICATION` markers in the implementation plan.

## 1. Technology Versions

**Task**: Inspect `android/build.gradle.kts` and `android/app/build.gradle.kts` to determine the versions of Kotlin and Jetpack Compose.

**Decision**:
- **Kotlin Version**: `1.9.22`
- **Jetpack Compose BOM**: `2023.08.00`
- **Jetpack Compose Compiler Extension**: `1.5.10`

**Rationale**: These versions are explicitly defined in the project's Gradle build files (`android/build.gradle.kts` and `android/app/build.gradle.kts`). Adhering to these versions ensures compatibility with the existing codebase.

**Alternatives considered**: None. The project has established versions.

## 2. Dynamic Theming in Jetpack Compose

**Task**: Research best practices for creating and applying custom color schemes in Jetpack Compose for dynamic theming.

**Decision**: The implementation will use Jetpack Compose's `MaterialTheme` to create a custom, dynamic theme for the keypad.

**Rationale**: This approach is the standard and recommended way to handle theming in Jetpack Compose. It provides a structured and maintainable way to manage colors, typography, and shapes.

The implementation will involve:
1.  **Creating a `Color.kt` file**: This file will define the specific color palettes for the "green" (income) and "red" (expense) themes.
2.  **Creating a `Theme.kt` file**: This file will contain a custom composable function (e.g., `KeypadTheme`) that wraps `MaterialTheme`. This function will accept a parameter to determine which color scheme (`green` or `red`) to apply.
3.  **Using `ColorScheme`**: The `KeypadTheme` composable will pass the appropriate `ColorScheme` object to the `MaterialTheme` based on the input parameter.

This approach allows for easy switching between themes and encapsulates the theming logic in a reusable and organized manner.

**Alternatives considered**:
- **Hardcoding colors**: This was rejected as it leads to an unmaintainable and inconsistent UI. It violates the principle of separation of concerns.
- **Using multiple `MaterialTheme` wrappers**: This was considered but deemed overly complex for this use case. A single, dynamic theme is more efficient.
