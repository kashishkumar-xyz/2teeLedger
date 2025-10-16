# Research Findings: Android UI Development

**Date**: 2025-10-16

## 1. JNI Integration with Rust

**Decision**: Use a **standard JNI bridge** to the existing C-style FFI functions exposed by the Rust library.

**Rationale**:
- **Existing Implementation**: The codebase already contains a complete and tested C-style FFI layer (`ledger/src/ffi.rs`). This provides a stable and predictable foundation for the Android integration.
- **Robustness**: A direct JNI-to-C-FFI integration is a standard, well-understood, and highly robust pattern for connecting Android to native Rust code. It avoids introducing additional third-party dependencies like JNA.
- **Performance**: This direct approach offers the best possible performance, as there is no intermediate abstraction layer between the JVM and the native code.

**Alternatives considered**:
- **JNA (Java Native Access)**: While JNA can reduce boilerplate for simple cases, it was deemed an unnecessary abstraction given that a comprehensive C-style FFI was already in place. Sticking to the existing FFI reduces dependencies and potential points of failure.

## 2. Secure Key Management at JNI Boundary

**Decision**: The Android app will retrieve the database encryption key from the Android Keystore. The raw key bytes will be passed to the Rust library's initialization function as a `ByteArray`. The Rust library will be responsible for zeroing the key from its memory after use.

**Rationale**:
- **Security**: The key's lifecycle in Android memory is minimized. It is retrieved from the secure hardware-backed Keystore and immediately passed over the JNI boundary.
- **Clear Responsibility**: The Android layer is responsible for platform-specific secure storage (Keystore), and the Rust layer is responsible for its own internal memory security (`zeroize`). This aligns with the Security-First principle.

**Alternatives considered**:
- **Storing the key in a global static variable**: Highly insecure and violates the project's core principles.
- **Passing the key via file**: Introduces unnecessary I/O and increases the attack surface.
