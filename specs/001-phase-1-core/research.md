# Research: Core Rust Library Development

This document outlines research and decisions made during the specification clarification phase.

## Encryption Key Management

**Decision**: Android Keystore (hardware-backed)

**Rationale**: Leveraging the Android Keystore provides the strongest security posture on Android by utilizing hardware-backed security features. This minimizes the exposure of the encryption key and protects it against various attacks, aligning with the project's "Security-First Design" principle.

**Alternatives Considered**: 
- User-provided passphrase (derived key): Rejected due to higher user friction and potential for weaker keys if not managed properly by the user.
- Combination of both (Keystore for KEK, user for passphrase): More complex implementation for initial phase, deferred for future consideration if advanced key rotation or user-specific keys are required.

## Corrupted Database Handling

**Decision**: Attempt automated recovery, notify user if failed, and revert to last healthy state using Version Control System (VCS).

**Rationale**: This approach prioritizes data integrity and user experience. Automated recovery attempts to fix minor corruption without user intervention. If recovery fails, clear communication to the user is essential, along with a mechanism to restore data. Utilizing VCS (e.g., Git for the database file itself, or a separate backup mechanism managed by the app that mimics VCS behavior) provides a robust way to revert to a known good state, aligning with the "Security-First Design" and "Append-Only Ledger" principles by preserving history and ensuring data recoverability.

**Alternatives Considered**:
- Immediately report unrecoverable error: Rejected as it offers no recovery path and leads to poor user experience.
- Silently fail and create new database: Rejected as it leads to silent data loss, which is unacceptable for a ledger application.