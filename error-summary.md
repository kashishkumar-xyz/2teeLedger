# SQLCipher Integration Error Summary

**Date**: 2025-10-18

## Project Setup and Architecture

**Goal**: Develop an Android frontend for the `teeLedger` Rust core library. The Android application acts as a view layer, interacting with the Rust core via a Foreign Function Interface (FFI). All business logic, database management, and encryption are handled by the secure Rust core.

**Rust Core (`ledger` crate)**:
-   **Language**: Rust
-   **Database**: `rusqlite` with `sqlcipher` feature for encrypted SQLite.
-   **FFI**: Exposes C-compatible functions for Android to interact with.
-   **Build Process**: Cross-compiled for Android ABIs (`arm64-v8a`, `x86`) using `cargo ndk`.

**Android Frontend (`android` project)**:
-   **Language**: Kotlin, Jetpack Compose
-   **FFI Bridge**: Uses JNA (`net.java.dev.jna`) to call Rust FFI functions.
-   **SQLCipher Integration**: Intended to use `net.zetetic:sqlcipher-android` AAR for native SQLCipher libraries.
-   **Target ABIs**: `arm64-v8a` (devices), `x86` (emulators).
-   **Development Environment**: Android Studio, bundled NDK and Gradle.

## Development Environment Setup

-   **OS**: Linux
-   **Project Root**: `/home/kaz/Dev/rust/_current/2teeLedger`
-   **Android NDK**: Installed via Android Studio SDK Manager, located at `/home/kaz/Android/Sdk/ndk/29.0.14206865`.
-   **`ANDROID_NDK_HOME`**: Environment variable set to the NDK path.
-   **Rust Targets**: `aarch64-linux-android` and `i686-linux-android` installed.

## Issues and Errors Encountered

The primary issue is a persistent `java.lang.UnsatisfiedLinkError` at runtime, indicating that native libraries, specifically `libledger_lib.so` and subsequently `libsqlcipher.so`, cannot be found by the Android runtime.

**Initial Error**: `java.lang.UnsatisfiedLinkError: Unable to load library 'ledger_lib': dlopen failed: library "libledger_lib.so" not found`

**Subsequent Error (after `libledger_lib.so` was seemingly found)**: `java.lang.UnsatisfiedLinkError: dlopen failed: library "libsqlcipher.so" not found: needed by .../libledger_lib.so`

## Attempted Fixes and Their Outcomes

1.  **Rust `rusqlite` features**:
    *   **Attempt**: Initially used `rusqlite = { features = ["sqlcipher"] }`. This failed to build due to `openssl/crypto.h` not found.
    *   **Attempt**: Switched to `rusqlite = { features = ["sqlcipher", "bundled-sqlcipher-vendored-openssl"] }`. This also failed to build, still related to OpenSSL.
    *   **Attempt**: Reverted to `rusqlite = { features = ["sqlcipher"] }` and introduced a `build.rs` script to link against manually extracted `libsqlcipher.so` files. This failed with `could not find native static library `sqlcipher``.
    *   **Outcome**: The Rust build process for `sqlcipher` has been highly problematic, suggesting `rusqlite`'s bundling features or manual linking with extracted AAR `.so` files are not straightforward for Android cross-compilation.

2.  **Android Gradle Configuration for Native Libraries**:
    *   **Attempt**: Added `ndk { abiFilters.addAll(listOf("x86", "x86_64", "arm64-v8a")) }` to `build.gradle.kts` to explicitly include ABIs.
    *   **Attempt**: Set `packaging { jniLibs { useLegacyPackaging = true } }` in `build.gradle.kts`.
    *   **Attempt**: Added `packaging { jniLibs { doNotStrip "**/*.so" } }` to `build.gradle.kts` to prevent stripping.
    *   **Attempt**: Added `pickFirsts` for `libledger_lib.so` and `libsqlcipher.so` to `packagingOptions` to explicitly include them.
    *   **Attempt**: Simplified `abiFilters` to `arm64-v8a` only, then re-added `x86` for emulator compatibility.
    *   **Outcome**: Despite these configurations, the native libraries are consistently reported as "not found" at runtime.

3.  **Native Library Placement**:
    *   **Attempt**: Manually copied `libledger_lib.so` files from Rust `target` directory to `android/app/src/main/jniLibs/<ABI>/`.
    *   **Attempt**: As a hack, renamed `jniLibs/x86_64` to `jniLibs/android-x86` based on an error message, which caused a build error. Reverted this.
    *   **Outcome**: Manual copying confirmed the files were present in the `jniLibs` directory, but the Android runtime still failed to find them.

4.  **JNA Loading**:
    *   **Attempt**: Explicitly loaded `System.loadLibrary("ledger_lib")` in `LedgerRepository.kt`'s `companion object`.
    *   **Attempt**: Explicitly loaded `System.loadLibrary("jnidispatch")` before `System.loadLibrary("ledger_lib")`.
    *   **Attempt**: Explicitly loaded `System.loadLibrary("sqlcipher")` before `System.loadLibrary("ledger_lib")`.
    *   **Outcome**: These changes did not resolve the `UnsatisfiedLinkError`.

5.  **NDK Version/Compatibility**:
    *   **Attempt**: Added `android.useDeprecatedNdk=true` to `gradle.properties`, which later caused a build error due to deprecation. Removed it.
    *   **Outcome**: This path was a dead end.

## Why It Hasn't Been Fixed Yet / Potential Issues

The core problem seems to be a disconnect between where Gradle *thinks* it's packaging the native libraries and where the Android runtime *actually* looks for them, or a deeper issue with how `libsqlite3-sys` (and thus `rusqlite`) is interacting with the Android NDK toolchain and the `sqlcipher` library.

**Potential Issues**:
-   **Gradle Packaging Bug/Misconfiguration**: Despite explicit `abiFilters` and `pickFirsts`, Gradle might not be correctly embedding `libledger_lib.so` and `libsqlcipher.so` into the final APK in a way the Android runtime expects.
-   **JNA Interaction**: There might be subtle requirements for JNA's native library loading on Android that are not being met, especially concerning its interaction with other native libraries like `sqlcipher`.
-   **`libsqlite3-sys` / `sqlcipher` Build Process**: The `build.rs` script's configuration for `libsqlite3-sys` to link against the extracted `libsqlcipher.so` might still be incorrect, leading to `libledger_lib.so` having an unresolved dependency at runtime.
-   **NDK Toolchain/Environment**: While `ANDROID_NDK_HOME` is set, there could be other environment variables or toolchain configurations that `libsqlite3-sys` or `sqlcipher` expect during compilation that are not correctly propagated.
-   **ABI Mismatch/Naming**: The Android runtime's specific search paths for native libraries might differ from what we are providing, or there's an unexpected ABI alias (e.g., `android-x86` vs `x86`).

## Current Status

The application builds successfully, but crashes at runtime with `java.lang.UnsatisfiedLinkError: dlopen failed: library "libsqlcipher.so" not found: needed by .../libledger_lib.so`. This indicates that `libledger_lib.so` is being loaded, but it cannot find its dependency `libsqlcipher.so`.

## Next Steps

Given the complexity and persistence of this issue, a more in-depth, manual debugging approach is required, potentially involving:
-   **APK Inspection**: Manually inspecting the generated APK to verify the exact contents and structure of the `lib/` directories.
-   **Detailed Gradle Logs**: Analyzing verbose Gradle build logs (`--info` or `--debug`) for clues during the native library merging and packaging phases.
-   **JNA/SQLCipher Android Examples**: Consulting working examples of JNA and SQLCipher integration on Android to identify any missing configurations.
