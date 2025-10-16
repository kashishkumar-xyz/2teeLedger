# FFI Contract: Android to Rust Core

**Date**: 2025-10-17

This document defines the functional contract between the Android UI (Kotlin) and the Rust core library (`libledgercore.so`) via the JNI. The UI will interact with the Rust library by declaring `external fun` functions that correspond to the C-style FFI functions exported by the Rust library.

## FFI Function Signatures

The following C-style functions are exported from the Rust library and must be matched by the Kotlin `external fun` declarations. All string parameters and return values are UTF-8 encoded C strings (`*const c_char`).

```c
// Initializes the database. Returns a C string (success or error message).
const char* init_db(const char* db_path, const char* encryption_key);

// Opens the database. Returns a C string (success or error message).
const char* open_db(const char* db_path, const char* encryption_key);

// Adds a transaction. Returns a C string (success or error message).
const char* add_transaction(const char* db_path, const char* encryption_key, const char* person, int64_t amount, const char* date, const char* note);

// Lists all transactions. Returns a JSON string or an error message.
const char* list_transactions(const char* db_path, const char* encryption_key, const char* person, const char* since_date, int32_t limit);

// Lists all balances. Returns a JSON string or an error message.
const char* list_balances(const char* db_path, const char* encryption_key);

// Gets the balance for a specific person. Returns the balance as a string or an error message.
const char* get_balance(const char* db_path, const char* encryption_key, const char* person);

// Backs up the database. Returns a C string (success or error message).
const char* backup_db(const char* db_path, const char* backup_path);

// Restores the database. Returns a C string (success or error message).
const char* restore_db(const char* backup_path, const char* db_path);
```

## Data Transfer Objects (DTOs) via JSON

For functions that return lists of objects (`list_transactions`, `list_balances`), the returned C string is a JSON payload. The Android application is responsible for parsing this JSON.

- **`BalanceSummary`**: `{"person": "John Doe", "balance": 1500}`
- **`Transaction`**: `{"id": 1, "person": "Jane Doe", "amount": -500, "date": "2025-10-17", "note": "Lunch"}`

**Note on Memory Management**: The C strings returned by the FFI functions are allocated by the Rust library. The Android/JNI side is responsible for freeing these strings after use to prevent memory leaks.