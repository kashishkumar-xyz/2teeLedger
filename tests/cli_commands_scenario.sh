#!/bin/bash

# Define variables for database path, backup path, and encryption key
DB_PATH="/tmp/tLegdger/ledger_scenario.db"
BACKUP_PATH="/tmp/tLegdger/ledger_scenario_backup.db"
RESTORED_DB_PATH="/tmp/tLegdger/ledger_scenario_restored.db"
ENCRYPTION_KEY="my_dummy_encryption_key_123"

echo "--- Initializing Database ---"
cargo run -- init-db --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY"
echo ""

echo "--- Adding Transactions for 4 Persons (3 positive, 2 negative each) ---"

# Person 1: Alice
echo "Adding transactions for Alice..."
cargo run -- add --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --person "Alice" --amount "100" --date "2025-01-01" --note "Initial deposit"
cargo run -- add --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --person "Alice" --amount "50" --date "2025-01-02" --note "Received payment"
cargo run -- add --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --person "Alice" --amount "-20" --date "2025-01-03" --note "Coffee"
cargo run -- add --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --person "Alice" --amount "75" --date "2025-01-04" --note "Bonus"
cargo run -- add --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --person "Alice" --amount "-30" --date "2025-01-05" --note "Groceries"
echo ""

# Person 2: Bob
echo "Adding transactions for Bob..."
cargo run -- add --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --person "Bob" --amount "200" --date "2025-01-06" --note "Project payment"
cargo run -- add --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --person "Bob" --amount "-40" --date "2025-01-07" --note "Dinner"
cargo run -- add --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --person "Bob" --amount "120" --date "2025-01-08" --note "Freelance work"
cargo run -- add --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --person "Bob" --amount "-60" --date "2025-01-09" --note "Rent"
cargo run -- add --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --person "Bob" --amount "80" --date "2025-01-10" --note "Gift"
echo ""

# Person 3: Charlie
echo "Adding transactions for Charlie..."
cargo run -- add --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --person "Charlie" --amount "300" --date "2025-01-11" --note "Investment"
cargo run -- add --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --person "Charlie" --amount "-70" --date "2025-01-12" --note "Utilities"
cargo run -- add --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --person "Charlie" --amount "90" --date "2025-01-13" --note "Sale"
cargo run -- add --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --person "Charlie" --amount "-25" --date "2025-01-14" --note "Snacks"
cargo run -- add --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --person "Charlie" --amount "110" --date "2025-01-15" --note "Refund"
echo ""

# Person 4: David
echo "Adding transactions for David..."
cargo run -- add --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --person "David" --amount "400" --date "2025-01-16" --note "Loan"
cargo run -- add --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --person "David" --amount "-80" --date "2025-01-17" --note "Repayment"
cargo run -- add --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --person "David" --amount "60" --date "2025-01-18" --note "Interest"
cargo run -- add --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --person "David" --amount "-15" --date "2025-01-19" --note "Parking"
cargo run -- add --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --person "David" --amount "130" --date "2025-01-20" --note "Commission"
echo ""

echo "--- Listing Transactions ---"
echo "All transactions:"
cargo run -- list --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY"
echo ""

echo "Transactions for Alice:"
cargo run -- list --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --person "Alice"
echo ""

echo "Transactions since 2025-01-10:"
cargo run -- list --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --since "2025-01-10"
echo ""

echo "Latest 3 transactions:"
cargo run -- list --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --limit 3
echo ""

echo "--- Checking Balances ---"
echo "All balances:"
cargo run -- balances --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY"
echo ""

echo "Balance for Alice:"
cargo run -- balance --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --person "Alice"
echo ""

echo "--- Backup and Restore ---"
echo "Backing up database to $BACKUP_PATH..."
cargo run -- backup-db --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY" --backup-path "$BACKUP_PATH"
echo ""

echo "Restoring database from $BACKUP_PATH to $RESTORED_DB_PATH..."
cargo run -- restore-db --backup-path "$BACKUP_PATH" --encryption-key "$ENCRYPTION_KEY" --db-path "$RESTORED_DB_PATH"
echo ""

echo "Verifying balance in restored database (Alice):"
cargo run -- balance --db-path "$RESTORED_DB_PATH" --encryption-key "$ENCRYPTION_KEY" --person "Alice"
echo ""

echo "--- Open DB Verification ---"
echo "Attempting to open DB with correct key (should succeed):"
cargo run -- open-db --db-path "$DB_PATH" --encryption-key "$ENCRYPTION_KEY"
echo ""

echo "Attempting to open DB with wrong key (should fail):"
cargo run -- open-db --db-path "$DB_PATH" --encryption-key "wrong_key"
echo ""

echo "--- Scenario Complete ---"
echo "You can now run this script using: bash tests/cli_commands_scenario.txt"
echo "Remember to delete the temporary database files if you wish to start fresh:"
echo "rm $DB_PATH $BACKUP_PATH $RESTORED_DB_PATH"
