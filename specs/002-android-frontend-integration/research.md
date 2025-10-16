# Research Findings: Android UI Development

**Date**: 2025-10-16

## 1. JNI/JNA Integration with Rust

**Decision**: Use the **Java Native Access (JNA)** library for the JNI bridge instead of manual JNI boilerplate.

**Rationale**:
- **Simplicity**: JNA significantly reduces the amount of boilerplate code required on the Kotlin/Java side. Instead of writing C-style JNI function declarations, we can define a simple Kotlin interface that maps directly to the Rust library's exported functions.
- **Maintainability**: This approach is cleaner and less error-prone. It makes the boundary between Kotlin and Rust easier to understand and manage.
- **Type Safety**: JNA provides better type mapping for common primitives and structs.

**Alternatives considered**:
- **Manual JNI**: Requires writing significant C/C++ glue code and using `external fun` declarations in Kotlin. This is complex, error-prone, and time-consuming.
- **JNIgen/other tools**: These tools can auto-generate bindings, but JNA provides a more direct and lightweight integration for this project's needs.

## 2. Secure Key Management at JNI Boundary

**Decision**: The Android app will retrieve the database encryption key from the Android Keystore. The raw key bytes will be passed to the Rust library's initialization function as a `ByteArray`. The Rust library will be responsible for zeroing the key from its memory after use.

**Rationale**:
- **Security**: The key's lifecycle in Android memory is minimized. It is retrieved from the secure hardware-backed Keystore and immediately passed over the JNI boundary.
- **Clear Responsibility**: The Android layer is responsible for platform-specific secure storage (Keystore), and the Rust layer is responsible for its own internal memory security (`zeroize`). This aligns with the Security-First principle.

**Alternatives considered**:
- **Storing the key in a global static variable**: Highly insecure and violates the project's core principles.
- **Passing the key via file**: Introduces unnecessary I/O and increases the attack surface.
