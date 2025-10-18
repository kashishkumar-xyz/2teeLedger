# FFI UI Bindings: Rust Core to Android UI

**Date**: 2025-10-18

This document specifies the Foreign Function Interface (FFI) contract between the `teeLedger` Rust core library and the Android UI. The Rust library will expose a C-compatible API, which the Android application will consume using JNA (Java Native Access).

## General Principles

- **C-ABI**: All exposed functions must use the C calling convention.
- **Memory Management**: Any memory allocated by Rust and passed to the UI (e.g., strings, lists) must be explicitly freed by the UI by calling a dedicated Rust function (e.g., `free_string`, `free_transaction_list`).
- **Error Handling**: Rust functions will return an integer status code (`0` for success, non-zero for errors). A dedicated function (`get_last_error`) can be called to retrieve a string representation of the last error that occurred on the current thread.
- **Threading**: All FFI calls are blocking and should be executed on a background thread on the Android side to avoid freezing the UI.

## Data Structures

### `Transaction` (FFI-safe)

Represents a single transaction. Corresponds to a JNA `Structure` on the Android side.

```rust
#[repr(C)]
pub struct Transaction {
    pub person: *const c_char,
    pub amount: f64,
    pub timestamp: i64,
    pub note: *const c_char, // Can be null
}
```

### `Balance` (FFI-safe)

Represents the total balance for a person.

```rust
#[repr(C)]
pub struct Balance {
    pub person: *const c_char,
    pub total: f64,
}
```

## Functions

### Database Management

#### `open_database`

Opens the encrypted ledger database.

- **Rust Signature**: `fn open_database(path: *const c_char, passphrase: *const c_char) -> i32`
- **Parameters**:
    - `path`: The absolute path to the database file.
    - `passphrase`: The encryption key.
- **Returns**: `0` on success, non-zero on failure.

### Transaction Management

#### `add_transaction`

Adds a new transaction to the ledger.

- **Rust Signature**: `fn add_transaction(person: *const c_char, amount: f64, note: *const c_char) -> i32`
- **Parameters**:
    - `person`: The name of the person.
    - `amount`: The transaction amount.
    - `note`: An optional note (can be null).
- **Returns**: `0` on success, non-zero on failure.

#### `get_all_balances`

Retrieves a list of all balances.

- **Rust Signature**: `fn get_all_balances(len: *mut u32) -> *const Balance`
- **Parameters**:
    - `len`: An output parameter that will be filled with the number of items in the returned list.
- **Returns**: A pointer to an array of `Balance` structs. The caller is responsible for freeing this memory by calling `free_balance_list`.

#### `get_transactions_for_person`

Retrieves all transactions for a specific person.

- **Rust Signature**: `fn get_transactions_for_person(person: *const c_char, len: *mut u32) -> *const Transaction`
- **Parameters**:
    - `person`: The name of the person.
    - `len`: An output parameter that will be filled with the number of items in the returned list.
- **Returns**: A pointer to an array of `Transaction` structs. The caller is responsible for freeing this memory by calling `free_transaction_list`.

### Memory Management

#### `free_string`

Frees a string that was allocated by Rust.

- **Rust Signature**: `fn free_string(s: *mut c_char)`

#### `free_balance_list`

Frees a list of `Balance` structs.

- **Rust Signature**: `fn free_balance_list(ptr: *mut Balance, len: u32)`

#### `free_transaction_list`

Frees a list of `Transaction` structs.

- **Rust Signature**: `fn free_transaction_list(ptr: *mut Transaction, len: u32)`

### Error Handling

#### `get_last_error`

Gets the last error message.

- **Rust Signature**: `fn get_last_error() -> *const c_char`
- **Returns**: A string with the last error message. The caller must free this string using `free_string`.
