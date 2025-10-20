# UI State Contract: KeypadScreen

**Branch**: `004-ui-screen-keypad` | **Date**: 2025-10-21 | **Spec**: [./spec.md](./spec.md)

This document defines the contract for the UI state of the Keypad screen. The state will be managed by a `KeypadViewModel` and exposed to the `KeypadScreen` composable.

## State Holder: `KeypadViewModel`

The `KeypadViewModel` will be responsible for holding and processing the keypad state.

```kotlin
class KeypadViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(KeypadState())
    val uiState: StateFlow<KeypadState> = _uiState.asStateFlow()

    fun onEvent(event: KeypadEvent) {
        // Handle events and update state
    }
}
```

## State Data Class: `KeypadState`

The `KeypadState` data class represents the state of the UI.

```kotlin
data class KeypadState(
    val displayText: String = "0",
    val theme: KeypadTheme = KeypadTheme.GREEN
)
```

## UI Events: `KeypadEvent`

A sealed class will be used to represent all possible user interactions (events) on the keypad screen.

```kotlin
sealed class KeypadEvent {
    data class Number(val number: Int) : KeypadEvent()
    object Decimal : KeypadEvent()
    object Backspace : KeypadEvent()
    object Clear : KeypadEvent()
}
```

## Theme: `KeypadTheme`

An enum will define the possible themes.

```kotlin
enum class KeypadTheme {
    GREEN,
    RED
}
```