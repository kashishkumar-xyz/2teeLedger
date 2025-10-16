# 2teeLedger CLI Usage Guide

This guide provides instructions on how to use the `2teeLedger` Command Line Interface (CLI) to manage your financial transactions securely.

## Installation

(Installation instructions will be provided in the main `README.md` or a dedicated installation guide.)

## Basic Usage

The `2teeLedger` CLI follows a standard command-line interface pattern:

```bash
cli [COMMAND] [OPTIONS]
```

To get general help or help for a specific command, use the `--help` flag:

```bash
cli --help
cli add --help
```

## Commands

Here's a detailed breakdown of each available command:

### `init-db` - Initialize the database

Initializes a new encrypted SQLite database at the specified path.

**Syntax:**

```bash
cli init-db --db-path <DB_PATH> --encryption-key <ENCRYPTION_KEY>
```

**Arguments:**

*   `--db-path <DB_PATH>`: The absolute path where the database file will be created.
*   `--encryption-key <ENCRYPTION_KEY>`: The encryption key to secure the database. **Keep this key secure!**

**Example:**

```bash
cli init-db --db-path /home/user/my_ledger.db --encryption-key "my-secret-key-123"
```

### `add` - Add a new transaction

Records a new financial transaction to the ledger.

**Syntax:**

```bash
cli add --db-path <DB_PATH> --encryption-key <ENCRYPTION_KEY> --person <PERSON> --amount <AMOUNT> --date <DATE> [--note <NOTE>]
```

**Arguments:**

*   `--db-path <DB_PATH>`: The absolute path to the database file.
*   `--encryption-key <ENCRYPTION_KEY>`: The encryption key for the database.
*   `--person <PERSON>`: The name of the person involved in the transaction (e.g., "Alice", "Bob").
*   `--amount <AMOUNT>`: The transaction amount. Can be positive (money received) or negative (money spent). (e.g., `100`, `-50`).
*   `--date <DATE>`: The date of the transaction in `YYYY-MM-DD` format (e.g., `2025-01-01`).
*   `--note <NOTE>` (Optional): A short description or note for the transaction.

**Examples:**

```bash
cli add --db-path /home/user/my_ledger.db --encryption-key "my-secret-key-123" --person Alice --amount 100 --date 2025-10-26 --note "Birthday gift"
cli add --db-path /home/user/my_ledger.db --encryption-key "my-secret-key-123" --person Bob --amount -50 --date 2025-10-27 --note "Coffee with friends"
```

### `list` - List transactions

Displays a list of all recorded transactions, with optional filtering.

**Syntax:**

```bash
cli list --db-path <DB_PATH> --encryption-key <ENCRYPTION_KEY> [--person <PERSON>] [--since-date <DATE>] [--limit <LIMIT>]
```

**Arguments:**

*   `--db-path <DB_PATH>`: The absolute path to the database file.
*   `--encryption-key <ENCRYPTION_KEY>`: The encryption key for the database.
*   `--person <PERSON>` (Optional): Filter transactions by a specific person.
*   `--since-date <DATE>` (Optional): Filter transactions from a specific date onwards (`YYYY-MM-DD`).
*   `--limit <LIMIT>` (Optional): Limit the number of transactions returned.

**Examples:**

```bash
cli list --db-path /home/user/my_ledger.db --encryption-key "my-secret-key-123"
cli list --db-path /home/user/my_ledger.db --encryption-key "my-secret-key-123" --person Alice
cli list --db-path /home/user/my_ledger.db --encryption-key "my-secret-key-123" --since-date 2025-10-01 --limit 5
```

### `balance` - Get balance for a specific person

Retrieves the aggregated balance for a single person.

**Syntax:**

```bash
cli balance --db-path <DB_PATH> --encryption-key <ENCRYPTION_KEY> --person <PERSON>
```

**Arguments:**

*   `--db-path <DB_PATH>`: The absolute path to the database file.
*   `--encryption-key <ENCRYPTION_KEY>`: The encryption key for the database.
*   `--person <PERSON>`: The name of the person whose balance you want to retrieve.

**Example:**

```bash
cli balance --db-path /home/user/my_ledger.db --encryption-key "my-secret-key-123" --person Alice
```

### `balances` - List all balances

Displays the aggregated balances for all people in the ledger.

**Syntax:**

```bash
cli balances --db-path <DB_PATH> --encryption-key <ENCRYPTION_KEY>
```

**Arguments:**

*   `--db-path <DB_PATH>`: The absolute path to the database file.
*   `--encryption-key <ENCRYPTION_KEY>`: The encryption key for the database.

**Example:**

```bash
cli balances --db-path /home/user/my_ledger.db --encryption-key "my-secret-key-123"
```

### `backup-db` - Backup the database

Creates a backup of the encrypted database to a specified location.

**Syntax:**

```bash
cli backup-db --db-path <DB_PATH> --encryption-key <ENCRYPTION_KEY> --backup-path <BACKUP_PATH>
```

**Arguments:**

*   `--db-path <DB_PATH>`: The absolute path to the source database file.
*   `--encryption-key <ENCRYPTION_KEY>`: The encryption key for the database.
*   `--backup-path <BACKUP_PATH>`: The absolute path where the backup file will be saved.

**Example:**

```bash
cli backup-db --db-path /home/user/my_ledger.db --encryption-key "my-secret-key-123" --backup-path /home/user/backups/my_ledger_backup.db
```

### `restore-db` - Restore the database

Restores an encrypted database from a backup file to a new location.

**Syntax:**

```bash
cli restore-db --backup-path <BACKUP_PATH> --encryption-key <ENCRYPTION_KEY> --db-path <DB_PATH>
```

**Arguments:**

*   `--backup-path <BACKUP_PATH>`: The absolute path to the backup file.
*   `--encryption-key <ENCRYPTION_KEY>`: The encryption key for the database.
*   `--db-path <DB_PATH>`: The absolute path where the restored database file will be created.

**Example:**

```bash
cli restore-db --backup-path /home/user/backups/my_ledger_backup.db --encryption-key "my-secret-key-123" --db-path /home/user/restored_ledger.db
```

### `open-db` - Open the database

Attempts to open an encrypted database with the provided key. This command is useful for verifying key correctness without performing other operations.

**Syntax:**

```bash
cli open-db --db-path <DB_PATH> --encryption-key <ENCRYPTION_KEY>
```

**Arguments:**

*   `--db-path <DB_PATH>`: The absolute path to the database file.
*   `--encryption-key <ENCRYPTION_KEY>`: The encryption key for the database.

**Example:**

```bash
cli open-db --db-path /home/user/my_ledger.db --encryption-key "my-secret-key-123"
```

## Error Handling

If a command encounters an error (e.g., incorrect encryption key, invalid input), it will print an error message to `stderr` and exit with a non-zero status code. This allows for scripting and automated error detection.

## Notes

*   **Security:** Always keep your encryption keys secure. Losing your key means losing access to your data.
*   **Paths:** All file paths (`--db-path`, `--backup-path`) must be absolute paths.
