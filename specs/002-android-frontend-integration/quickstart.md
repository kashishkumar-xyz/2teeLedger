# Quickstart: Android UI Development

**Date**: 2025-10-16

This guide provides the basic steps to set up the development environment and run the teeLedger Android application.

## Prerequisites

1.  **Android Studio**: Latest stable version.
2.  **Java Development Kit (JDK)**: Version 17 or higher.
3.  **Rust Toolchain**: Install via `rustup`.
4.  **cargo-ndk**: Install via `cargo install cargo-ndk`.
5.  **Android NDK**: Install via the Android Studio SDK Manager.

## Build and Run

1.  **Clone the repository**.
2.  **Open the project in Android Studio**.
3.  **Configure Gradle**: The `app/build.gradle.kts` file is configured to execute a `cargo-ndk` build task before the main application build. This will compile the Rust core library (`libledgercore`) for all target Android ABIs.
4.  **Sync Gradle**: Let Android Studio download all dependencies.
5.  **Run the app**: Select an emulator or connect a physical device and click the 'Run' button in Android Studio. The Gradle script will handle the entire build process automatically.

## Project Structure

- The Android application code is located in `app/`.
- The Rust core library code is located in `src/` (at the repository root).
- The compiled `.so` files from the Rust library are automatically placed in `app/src/main/jniLibs/` by the build script.
