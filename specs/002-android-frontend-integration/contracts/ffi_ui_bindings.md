# FFI Contract: UI to Rust Core

**Date**: 2025-10-16

This document defines the functional contract between the Android UI (Kotlin) and the Rust core library (`libledgercore.so`) via the JNI/JNA bridge.

The UI will interact with the Rust library through an interface with the following methods.

## LedgerCore Interface

```kotlin
// Assumed to be a JNA interface definition
interface LedgerCore : Library {

    /**
     * Initializes the database with the provided encryption key.
     * Must be called before any other function.
     */
    fun init_db(key: ByteArray): String? // Returns null on success, error string on failure

    /**
     * Retrieves a list of all balance summaries.
     * Returns a JSON string representing a list of BalanceSummary objects.
     */
    fun get_balances(): String // JSON: Result<List<BalanceSummary>, String>

    /**
     * Retrieves the transaction history for a specific person.
     * Returns a JSON string representing a list of TransactionDetails objects.
     */
    fun get_transaction_history(personName: String): String // JSON: Result<List<TransactionDetails>, String>

    /**
     * Adds a new transaction to the ledger.
     * Returns null on success, error string on failure.
     */
    fun add_transaction(personName: String, amount: Long, note: String?): String?
}
```

**Data Transfer Objects (DTOs) via JSON:**

To simplify the FFI boundary, complex objects will be serialized to JSON strings.

- **`BalanceSummary`**: `{"person_name": "John Doe", "net_balance": 1500}`
- **`TransactionDetails`**: `{"id": "tx123", "date": "2025-10-16", "amount": 500, "note": "Lunch"}`
- **`Result<T, E>`**: A standard Rust-style result, e.g., `{"Ok": [...]}` or `{"Err": "Database is locked"}`.
