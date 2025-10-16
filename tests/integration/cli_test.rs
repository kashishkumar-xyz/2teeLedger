use assert_cmd::prelude::*;
use predicates::prelude::*;
use std::process::Command;
use tempfile::NamedTempFile;
use rusqlite::Connection;

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
