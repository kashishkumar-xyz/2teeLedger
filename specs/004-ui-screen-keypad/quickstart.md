# Quickstart: Keypad UI Screen

**Date**: 2025-10-21

This document provides instructions on how to run and test the new Keypad UI screen.

## Prerequisites

- Android Studio installed and configured.
- An Android emulator or a physical device connected.

## Running the Keypad Screen

Since the Keypad screen is being developed in isolation, you need to temporarily change the application's entry point to launch this screen directly.

1.  **Open the project** in Android Studio.
2.  **Navigate** to `android/app/src/main/java/com/example/ledger/ui/MainActivity.kt`.
3.  **Modify the `setContent` block** in the `MainActivity` class to call the `KeypadScreen` composable instead of the main navigation graph.

    **From (example):**
    ```kotlin
    setContent {
        LedgerTheme {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                AppNavigation()
            }
        }
    }
    ```

    **To:**
    ```kotlin
    setContent {
        KeypadTheme(theme = KeypadTheme.GREEN) { // Or KeypadTheme.RED
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                KeypadScreen()
            }
        }
    }
    ```

4.  **Build and run** the application on your emulator or device. The Keypad screen should appear on launch.

## Running Tests

Instrumented tests for the Keypad screen are located in `androidTest/java/com/example/ledger/ui/keypad/KeypadScreenTest.kt`.

To run the tests:

1.  **Right-click** on the `KeypadScreenTest.kt` file in the Project view.
2.  **Select "Run 'KeypadScreenTest'"** from the context menu.

Alternatively, you can run all Android tests from the command line using Gradle:

```bash
cd android
./gradlew connectedAndroidTest
```
