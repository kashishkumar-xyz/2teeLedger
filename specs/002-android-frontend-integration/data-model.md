# Data Models: teeLedger UI

**Date**: 2025-10-16
**Source Spec**: /home/kaz/Dev/rust/_current/2teeLedger/specs/002-android-frontend-integration/spec.md

This document defines the data models (view models) used by the Android UI. These are distinct from the core data structures within the Rust library.

## 1. BalanceSummary

Represents the financial standing with a single person, intended for display on the main screen.

**Fields**:
- `person_name`: `String` - The name of the other party.
- `net_balance`: `Long` - The aggregated net balance. Positive if they owe the user, negative if the user owes them.

**Validation Rules**:
- `person_name` must not be empty.

## 2. TransactionDetails

Represents a single financial event for display in a transaction history list.

**Fields**:
- `id`: `String` - A unique identifier for the transaction.
- `date`: `String` - The date of the transaction (ISO 8601 format).
- `amount`: `Long` - The transaction amount.
- `note`: `String?` - An optional description of the transaction.

**Validation Rules**:
- `id` must not be empty.
- `amount` must not be zero.
