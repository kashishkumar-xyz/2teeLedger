# Research: Android FFI Integration

**Date**: 2025-10-18

This document outlines the key research areas required to successfully integrate the teeLedger Rust core with the Android frontend using a Foreign Function Interface (FFI).

## Research Tasks

### 1. Rust FFI Best Practices

- **Task**: Investigate and document best practices for creating a C-compatible FFI layer in Rust.
- **Questions to Answer**:
    - How should Rust structs be represented to be C-compatible (`#[repr(C)]`)?
    - How are strings (`CString`, `CStr`) safely passed and returned between Rust and C?
    - How should memory be managed? Who is responsible for freeing memory (e.g., for strings or structs returned from Rust)?
    - What is the standard way to handle and propagate errors across the FFI boundary (e.g., error codes, special return values)?
- **Resources**: The Rust FFI Omnibus, The Rustonomicon.

### 2. JNA on Android

- **Task**: Determine the feasibility and best practices for using JNA (Java Native Access) to call the Rust shared library (`.so`) from the Android application.
- **Questions to Answer**:
    - How is the JNA dependency added to a Gradle project?
    - How is the native `.so` library loaded on different Android architectures (ARM, x86)?
    - How is a JNA interface defined in Java/Kotlin to map to the Rust FFI functions?
    - What are the performance implications of using JNA on Android compared to JNI?
- **Alternatives**: JNI (Java Native Interface). JNA is preferred for its simplicity if performance is acceptable.

### 3. Data Type Mapping

- **Task**: Create a clear mapping of data types between Rust, the C FFI layer, and Kotlin/Java.
- **Questions to Answer**:
    - How do primitive types (e.g., `i32`, `f64`, `bool`) map between Rust and Java?
    - How are complex data structures (structs) mapped? This involves creating corresponding `Structure` classes in JNA.
    - How will lists or arrays of structs be passed from Rust to Kotlin?
- **Output**: A mapping table in `contracts/ffi_ui_bindings.md`.

### 4. Error Handling Strategy

- **Task**: Design a robust error handling strategy for operations that cross the FFI boundary.
- **Questions to Answer**:
    - Should Rust return integer error codes or a status enum?
    - How can the Android app retrieve detailed error messages from Rust without causing memory leaks?
    - How will these FFI errors be translated into user-friendly exceptions or states in the Android ViewModel?
- **Output**: A section in `contracts/ffi_ui_bindings.md` detailing the error handling contract.
