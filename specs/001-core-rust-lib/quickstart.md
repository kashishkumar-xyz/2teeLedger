# Quickstart: Core Rust Library Development

This guide provides a quick overview of how to set up, build, and test the core Rust library for teeLedger.

## Prerequisites

- Rust toolchain (version 1.75+ recommended)
- `cargo-ndk` (for Android cross-compilation)

## Building the Library

1.  **Clone the repository:**
    ```bash
    git clone [repository_url]
    cd teeLedger
    ```

2.  **Navigate to the Rust crate:**
    ```bash
    cd src/rust_core # Assuming 'rust_core' is the name of your Rust crate
    ```

3.  **Build for Android (example for arm64-v8a):**
    ```bash
    cargo ndk -t arm64-v8a build --release
    ```
    This will generate the shared library (`.so` file) in `target/aarch66-linux-android/release/`.

## Running Tests

1.  **Unit Tests:**
    ```bash
    cargo test
    ```

2.  **Integration Tests:**
    ```bash
    # If you have a separate integration test suite
    cargo test --workspace --test integration_tests
    ```

## FFI/JNI Integration

- The Rust library exposes functions via a C ABI, which can be called from Java/Kotlin using JNI.
- Refer to the `ffi.rs` file for the exposed function signatures.
- Example JNI calls will be provided in the Android application layer.
- **Important**: For secure database operations, ensure proper key management (e.g., using Android Keystore) and consider database recovery strategies as detailed in `spec.md` and `research.md`.

## Key Management & Backup/Restore

### Key Management Setup

1.  **Generate DEK**: Upon first application launch, a Data Encryption Key (DEK) should be generated.
2.  **Wrap DEK**: The DEK should be encrypted (wrapped) using a Key Encryption Key (KEK) stored in the Android Keystore.
3.  **Store Wrapped DEK**: The wrapped DEK is stored in the app's private file storage.
4.  **Runtime Access**: At runtime, retrieve the KEK from Keystore to unwrap the DEK, open the SQLCipher database, and immediately zeroize the DEK from memory.

### Manual Backup and Restore

1.  **Backup**: Use the `backup_db` FFI function to create an encrypted snapshot of the database. This will be stored in the `data/backups/` directory.
2.  **Restore**: Use the `restore_db` FFI function to restore the database from a chosen backup file. This operation should be performed with caution, as it will overwrite the current database.