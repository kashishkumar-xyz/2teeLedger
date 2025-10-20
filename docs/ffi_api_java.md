# LedgerApi.java Documentation

This document describes the `LedgerApi.java` interface, which serves as the Java Native Access (JNA) binding for the `ledger_lib` Rust library. It facilitates communication between the Android frontend and the secure Rust backend for ledger operations.

## Overview

`LedgerApi.java` defines the contract for interacting with the native Rust functions. It uses JNA to map Java method calls to their corresponding native implementations in the `ledger_lib`.

## `LedgerApi` Interface

```java
public interface LedgerApi extends Library {
    LedgerApi INSTANCE = Native.load("ledger_lib", LedgerApi.class);
    // ... native method declarations and structure definitions
}
```

-   `LedgerApi.INSTANCE`: This static field initializes the JNA interface by loading the native library named "ledger_lib". This library must be available in the Android application's `jniLibs` directory.

## Data Structures

### `Transaction`

Represents a financial transaction record.

```java
@Structure.FieldOrder({"person", "amount", "timestamp", "note"})
class Transaction extends Structure {
    public Pointer person;    // Pointer to a native string (UTF-8) representing the person involved.
    public double amount;    // The transaction amount.
    public long timestamp;   // Unix timestamp of the transaction.
    public Pointer note;      // Pointer to a native string (UTF-8) for an optional note.

    public Transaction() {}
    public Transaction(Pointer p) { super(p); }
}
```

-   `person`: A pointer to a null-terminated UTF-8 string representing the person associated with the transaction. This memory is managed by the native library and must be freed using `free_string` if converted to a Java string.
-   `amount`: The monetary value of the transaction.
-   `timestamp`: The time the transaction occurred, typically a Unix epoch timestamp.
-   `note`: A pointer to a null-terminated UTF-8 string containing an optional description or note for the transaction. This memory is managed by the native library and must be freed using `free_string` if converted to a Java string.

### `Balance`

Represents the balance for a specific person.

```java
@Structure.FieldOrder({"person", "total"})
class Balance extends Structure {
    public Pointer person;    // Pointer to a native string (UTF-8) representing the person.
    public double total;     // The total balance for the person.

    public Balance() {}
    public Balance(Pointer p) { super(p); }
}
```

-   `person`: A pointer to a null-terminated UTF-8 string representing the person whose balance is being reported. This memory is managed by the native library and must be freed using `free_string` if converted to a Java string.
-   `total`: The calculated total balance for the person.

## Native Methods

### `int open_database(String path, String passphrase)`

Opens and decrypts the ledger database.

-   **Parameters:**
    -   `path`: The absolute path to the database file.
    -   `passphrase`: The passphrase used to decrypt the database.
-   **Returns:** An integer status code (0 for success, non-zero for error).

### `int init_database()`

Initializes the database schema if it doesn't already exist.

-   **Parameters:** None.
-   **Returns:** An integer status code (0 for success, non-zero for error).

### `int add_transaction(String person, double amount, String note)`

Adds a new transaction to the ledger.

-   **Parameters:**
    -   `person`: The name of the person involved in the transaction.
    -   `amount`: The amount of the transaction.
    -   `note`: An optional note for the transaction.
-   **Returns:** An integer status code (0 for success, non-zero for error).

### `Pointer get_all_balances(int[] len)`

Retrieves a list of all current balances for all persons.

-   **Parameters:**
    -   `len`: An integer array of size 1. The native function will write the number of `Balance` structures returned into `len[0]`.
-   **Returns:** A `Pointer` to an array of `Balance` structures in native memory. This memory *must* be freed using `free_balance_list`.

### `Pointer get_transactions_for_person(String person, int[] len)`

Retrieves a list of all transactions for a specific person.

-   **Parameters:**
    -   `person`: The name of the person to retrieve transactions for.
    -   `len`: An integer array of size 1. The native function will write the number of `Transaction` structures returned into `len[0]`.
-   **Returns:** A `Pointer` to an array of `Transaction` structures in native memory. This memory *must* be freed using `free_transaction_list`.

### `void free_string(Pointer s)`

Frees memory allocated by the native library for a string.

-   **Parameters:**
    -   `s`: A `Pointer` to the native string memory to be freed.
-   **Returns:** Void.

### `void free_balance_list(Pointer ptr, int len)`

Frees memory allocated by the native library for a list of `Balance` structures.

-   **Parameters:**
    -   `ptr`: A `Pointer` to the start of the `Balance` array in native memory.
    -   `len`: The number of `Balance` structures in the array.
-   **Returns:** Void.

### `void free_transaction_list(Pointer ptr, int len)`

Frees memory allocated by the native library for a list of `Transaction` structures.

-   **Parameters:**
    -   `ptr`: A `Pointer` to the start of the `Transaction` array in native memory.
    -   `len`: The number of `Transaction` structures in the array.
-   **Returns:** Void.

### `Pointer get_last_error()`

Retrieves the last error message from the native library.

-   **Parameters:** None.
-   **Returns:** A `Pointer` to a native string (UTF-8) containing the last error message. This memory *must* be freed using `free_string`.

## Memory Management

It is crucial to properly manage memory when interacting with the native library. Any `Pointer` returned by native functions (e.g., from `get_all_balances`, `get_transactions_for_person`, `get_last_error`, or fields within `Transaction` and `Balance` structures) represents memory allocated by the Rust backend. This memory *must* be explicitly freed using the corresponding `free_` functions (`free_string`, `free_balance_list`, `free_transaction_list`) to prevent memory leaks.
