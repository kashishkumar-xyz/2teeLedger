# Quickstart: Database Improvements for Querying and Data Integrity

This guide provides a quick overview of how to use the CLI application with the improved database querying and data integrity features.

## Prerequisites

- Rust toolchain (latest stable version recommended)
- `cargo` build tool

## 1. Build the CLI Application

Navigate to the project root directory and build the CLI application in release mode:

```bash
car go build --release
```

This will create the executable at `target/release/cli`.

## 2. Initialize a New Database

Before adding any transactions, you need to initialize a database. Choose a path for your database file and an encryption key.

```bash
# Define your database path and encryption key
DB_PATH="/tmp/my_ledger.db"
ENCRYPTION_KEY="my_secure_key_123"

cargo run --release -- init-db --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY"
```

## 3. Add a Transaction

Now you can add transactions. The `person` and `date` fields are now stored in dedicated columns for efficient querying.

```bash
cargo run --release -- add \
  --db-path "$DB_PATH" \
  --encryption-key "$ENCRYPTION_KEY" \
  --person "Alice" \
  --amount "100" \
  --date "2025-01-01" \
  --note "Initial deposit"

cargo run --release -- add \
  --db-path "$DB_PATH" \
  --encryption-key "$ENCRYPTION_KEY" \
  --person "Bob" \
  --amount "-50" \
  --date "2025-01-02" \
  --note "Lunch with Bob"
```

## 4. List Transactions

You can list all transactions, or filter them by person, date, or limit the results.

### List all transactions:

```bash
cargo run --release -- list --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY"
```

### List transactions for a specific person:

```bash
cargo run --release -- list --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --person "Alice"
```

### List transactions since a specific date:

```bash
cargo run --release -- list --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --since "2025-01-02"
```

### List the latest N transactions:

```bash
cargo run --release -- list --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --limit 1
```

## 5. Check Balances

You can view the balance for all persons or a specific person.

### List all balances:

```bash
cargo run --release -- balances --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY"
```

### Get balance for a specific person:

```bash
cargo run --release -- balance --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --person "Alice"
```

## 6. Backup and Restore (Optional)

```bash
BACKUP_PATH="/tmp/my_ledger_backup.db"
RESTORED_DB_PATH="/tmp/my_ledger_restored.db"

cargo run --release -- backup-db --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --backup-path "$BACKUP_PATH"

cargo run --release -- restore-db --backup-path "$BACKUP_PATH" --encryption-key "$ENCRYPTION_KEY" --db-path "$RESTORED_DB_PATH"
```

## Cleanup

To remove the temporary database files created during this quickstart:

```bash
rm "$DB_PATH" "$BACKUP_PATH" "$RESTORED_DB_PATH"
```
