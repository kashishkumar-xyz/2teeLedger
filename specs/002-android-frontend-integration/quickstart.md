# Quickstart: Building and Running the Android App

**Branch**: `spec-002/android-frontend-integration` | **Date**: 2025-10-17

This guide provides the steps to build and run the teeLedger Android application.

## Prerequisites

- [Android Studio](https://developer.android.com/studio) (latest stable version)
- An Android Virtual Device (AVD) configured in Android Studio, or a physical Android device (API 24+).

## Build & Run Instructions

1.  **Open the Project**:
    -   Launch Android Studio.
    -   Select "Open" or "Open an Existing Project".
    -   Navigate to and select the `android/` directory within this repository.

2.  **Sync Gradle**:
    -   Android Studio will automatically detect the Gradle project and prompt you to sync. This will download all the required dependencies, including SQLCipher.
    -   Wait for the Gradle sync to complete successfully.

3.  **Run the App**:
    -   Select a run configuration (usually the `app` module is pre-selected).
    -   Choose your target device (either a running emulator or a connected physical device).
    -   Click the "Run" button (the green play icon).

## Expected Outcome

The application will build, install, and launch on the selected device. The main screen of the application will be displayed.