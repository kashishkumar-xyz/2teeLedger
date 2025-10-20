# Quickstart: Keypad UI Screen

**Branch**: `004-ui-screen-keypad` | **Date**: 2025-10-21 | **Spec**: [./spec.md](./spec.md)

This guide provides instructions on how to run and test the new Keypad UI screen.

## Prerequisites

- Android Studio installed.
- An Android emulator or a physical device connected.

## Running the Keypad Screen

To test the Keypad screen in isolation, the main entry point of the application needs to be temporarily modified.

1.  **Open the project** in Android Studio.

2.  **Navigate to the `MainActivity.kt` file** located at `android/app/src/main/java/com/example/ledger/ui/MainActivity.kt`.

3.  **Modify the `setContent` block** to launch the `KeypadScreen` directly. You can comment out the existing navigation setup.

    ```kotlin
    class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                LedgerTheme {
                    // Comment out the existing NavHost and related code
                    // NavHost(...) { ... }

                    // Add the KeypadScreen directly
                    KeypadScreen()
                }
            }
        }
    }
    ```

4.  **Run the app** on your emulator or device. The Keypad screen should be the first screen you see.

## Switching Themes

To test the different themes (green and red), you can pass the desired theme as a parameter to the `KeypadScreen` composable.

```kotlin
// In MainActivity.kt
KeypadScreen(theme = KeypadTheme.RED)
```

This allows you to easily switch between the themes for visual verification against the design mockups.