use assert_cmd::prelude::*;
use predicates::prelude::*;
use std::process::Command;
use tempfile::NamedTempFile;
use rusqlite::Connection;
use serde_json::Value;

#[test]
fn test_cli_add_transaction() -> Result<(), Box<dyn std::error::Error>> {
    let db_file = NamedTempFile::new()?;
    let db_path = db_file.path().to_str().unwrap();
    let key = "test_key";

    // 1. Initialize the database via the CLI
    let mut cmd = Command::cargo_bin("cli")?;
    cmd.arg("init-db").arg("--db-path").arg(db_path).arg("--encryption-key").arg(key);
    cmd.assert().success().stdout(predicate::str::contains("Database initialized successfully"));

    // 2. Add a transaction via the CLI
    let mut cmd = Command::cargo_bin("cli")?;
    cmd.arg("add")
        .arg("--db-path").arg(db_path) // Pass the same db_path
        .arg("--encryption-key").arg(key) // Pass the same key
        .arg("--person").arg("Alice")
        .arg("--amount").arg("100")
        .arg("--date").arg("2025-01-01");
    cmd.assert().success().stdout(predicate::str::contains("Transaction added successfully"));

    // 3. Verify the data was written correctly by opening the DB directly
    let conn = Connection::open(db_path)?;
    conn.pragma_update(None, "key", &key)?;
    let count: i64 = conn.query_row("SELECT COUNT(*) FROM transactions_history", [], |row| row.get(0))?;
    assert_eq!(count, 1);

    Ok(())
}

#[test]
fn test_cli_list_transactions() -> Result<(), Box<dyn std::error::Error>> {
    let db_file = NamedTempFile::new()?;
    let db_path = db_file.path().to_str().unwrap();
    let key = "test_key";

    // 1. Initialize the database via the CLI
    let mut cmd = Command::cargo_bin("cli")?;
    cmd.arg("init-db").arg("--db-path").arg(db_path).arg("--encryption-key").arg(key);
    cmd.assert().success().stdout(predicate::str::contains("Database initialized successfully"));

    // 2. Add some transactions via the CLI
    let mut cmd = Command::cargo_bin("cli")?;
    cmd.arg("add")
        .arg("--db-path").arg(db_path).arg("--encryption-key").arg(key)
        .arg("--person").arg("Alice").arg("--amount").arg("100").arg("--date").arg("2025-01-01");
    cmd.assert().success();

    let mut cmd = Command::cargo_bin("cli")?;
    cmd.arg("add")
        .arg("--db-path").arg(db_path).arg("--encryption-key").arg(key)
        .arg("--person").arg("Bob").arg("--amount").arg("200").arg("--date").arg("2025-01-02");
    cmd.assert().success();

    // 3. List all transactions via the CLI
    let mut cmd = Command::cargo_bin("cli")?;
    cmd.arg("list").arg("--db-path").arg(db_path).arg("--encryption-key").arg(key);
    let output = cmd.assert().success().stdout(predicate::str::is_empty().not()).get_output().stdout.clone();
    let transactions: Value = serde_json::from_slice(&output)?;

    assert!(transactions.is_array());
    assert_eq!(transactions.as_array().unwrap().len(), 2);
    assert_eq!(transactions.as_array().unwrap()[0]["person"], "Bob"); // Ordered by date DESC
    assert_eq!(transactions.as_array().unwrap()[1]["person"], "Alice");

    // 4. List transactions for Alice via the CLI
    let mut cmd = Command::cargo_bin("cli")?;
    cmd.arg("list").arg("--db-path").arg(db_path).arg("--encryption-key").arg(key)
        .arg("--person").arg("Alice");
    let output_alice = cmd.assert().success().stdout(predicate::str::is_empty().not()).get_output().stdout.clone();
    let alice_transactions: Value = serde_json::from_slice(&output_alice)?;

    assert!(alice_transactions.is_array());
    assert_eq!(alice_transactions.as_array().unwrap().len(), 1);
    assert_eq!(alice_transactions.as_array().unwrap()[0]["person"], "Alice");

    Ok(())
}