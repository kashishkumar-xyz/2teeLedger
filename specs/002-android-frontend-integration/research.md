# Research: Android Standalone Application

**Branch**: `spec-002/android-frontend-integration` | **Date**: 2025-10-17

This document records the key technical decisions for the teeLedger Android application, focusing on a stable, secure, and maintainable architecture.

---

## 1. Persistence and Encryption

**Decision**: Use **AndroidX Room** with **SQLCipher for Android** for the persistence layer.

**Rationale**:
- **Stability & Maintainability**: Room is the Google-recommended standard for persistence on Android. It provides a robust, type-safe abstraction over SQLite, reducing boilerplate and catching SQL errors at compile time.
- **Security**: By using the official `net.zetetic:sqlcipher-android` library via its `SupportOpenHelperFactory`, we can seamlessly integrate FIPS 140-2 validated, 256-bit AES encryption into Room. This meets our security-first principle without requiring custom build logic.
- **Developer Experience**: Room and its Kotlin Coroutines integration (`Flow`) simplify data access and observation, making it easy to build a reactive UI.

**Alternatives Considered**:
- **Direct SQLite with JNI Bridge**: The original approach. This was abandoned due to extreme build complexity, brittleness, and the difficulty of debugging native code. It coupled the Android app too tightly to the Rust backend.
- **Realm / other mobile databases**: While viable, Room is the standard component within the Jetpack suite and integrates most cleanly with other AndroidX libraries like ViewModel and Compose.

---

## 2. Application Architecture

**Decision**: Use the **Model-View-ViewModel (MVVM)** architecture with Jetpack Compose.

**Rationale**:
- **Lifecycle-Awareness**: ViewModels are designed to store and manage UI-related data in a lifecycle-conscious way, surviving configuration changes (like screen rotations) that would otherwise destroy the data.
- **Separation of Concerns**: MVVM creates a clear separation between the UI (the View, our Composables), the business logic/state holder (ViewModel), and the data source (the Repository/Room database). This makes the app easier to test, debug, and maintain.
- **Compose Integration**: Jetpack Compose is designed to work reactively with state. Using ViewModels with `StateFlow` provides a clean, efficient, and idiomatic way to connect our UI to the underlying data layer.

**Alternatives Considered**:
- **Model-View-Presenter (MVP)**: An older pattern that is less suited to the declarative and state-driven nature of Jetpack Compose.
- **No Architecture (Activity/Fragment-based logic)**: Placing all logic in the UI layer leads to tightly-coupled, untestable code that is difficult to maintain.