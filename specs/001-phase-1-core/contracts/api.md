# API Contracts: Core Rust Library Development

This document outlines the API contracts for the core Rust library, focusing on the functions exposed via FFI/JNI for mobile UI integration.

## Functions

### `add_transaction`

**Description**: Adds a new transaction to the ledger.
**Inputs**: `person` (string), `amount` (integer), `date` (string, YYYY-MM-DD), `note` (optional string).
**Outputs**: `transaction_id` (integer) on success, error on failure.

### `list_transactions`

**Description**: Retrieves a list of transactions.
**Inputs**: `person` (optional string), `since_date` (optional string, YYYY-MM-DD), `limit` (optional integer).
**Outputs**: List of `Transaction` objects on success, error on failure.

### `get_balance`

**Description**: Computes the current balance for a specific person.
**Inputs**: `person` (string).
**Outputs**: `balance_amount` (integer) on success, error on failure.

### `list_balances`

**Description**: Computes and returns the balances for all persons in the ledger.
**Inputs**: None.
**Outputs**: List of `Balance` objects on success, error on failure.

### `init_db`

**Description**: Initializes a new SQLite database with encryption. The `encryption_key` is expected to be retrieved from a secure source like the Android Keystore.
**Inputs**: `db_path` (string), `encryption_key` (byte array).
**Outputs**: Success/Error status.

### `open_db`

**Description**: Opens an existing encrypted SQLite database. The `encryption_key` is expected to be retrieved from a secure source like the Android Keystore.
**Inputs**: `db_path` (string), `encryption_key` (byte array).
**Outputs**: Success/Error status.

### `backup_db`

**Description**: Creates an encrypted backup of the database file.
**Inputs**: `db_path` (string), `encryption_key` (byte array), `backup_path` (string).
**Outputs**: Success/Error status.

### `restore_db`

**Description**: Restores a database from an encrypted backup file.
**Inputs**: `backup_path` (string), `encryption_key` (byte array), `db_path` (string).
**Outputs**: Success/Error status.