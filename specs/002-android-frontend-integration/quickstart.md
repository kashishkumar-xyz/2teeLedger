# Quickstart: Building and Running the Android UI

**Date**: 2025-10-18

This guide provides the steps to build the Rust core, integrate it into the Android project, and run the application.

## Prerequisites

- Android SDK (API Level 24+)
- Android NDK
- Rust toolchain with `aarch64-linux-android` and `x86_64-linux-android` targets installed:
  ```bash
  rustup target add aarch64-linux-android
  rustup target add x86_64-linux-android
  ```
- `cargo-ndk` for simplified cross-compilation:
  ```bash
  cargo install cargo-ndk
  ```

## Step 1: Compile the Rust Core for Android

From the `ledger` directory, compile the Rust library into a shared object (`.so`) for each target Android architecture.

```bash
# For ARM64 (most common on modern devices)
cargo ndk -t arm64-v8a --platform 24 -o ../android/app/src/main/jniLibs/arm64-v8a build --release

# For x86_64 (for emulators)
cargo ndk -t x86_64 --platform 24 -o ../android/app/src/main/jniLibs/x86_64 build --release
```

These commands will produce `libledger.so` files in the correct `jniLibs` directories within the Android project structure.

## Step 2: Build and Run the Android Application

1.  Open the `android` directory in Android Studio.
2.  Android Studio should automatically sync the Gradle project.
3.  Ensure you have a connected device or a running emulator.
4.  Build and run the application using the standard "Run 'app'" command (Shift+F10).

Gradle will automatically package the `.so` files from the `jniLibs` directory into the final APK.

## How it Works

- The `LedgerApi.java` interface uses JNA to discover and bind to the native functions in `libledger.so` at runtime.
- The `LedgerRepository.kt` provides a clean, coroutine-based Kotlin API over the JNA interface.
- The `LedgerViewModel.kt` calls the repository functions (on a background thread) to interact with the Rust core.
- The Jetpack Compose UI observes the ViewModel for state changes and displays the data.
