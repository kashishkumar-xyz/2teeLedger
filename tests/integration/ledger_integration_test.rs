use assert_cmd::prelude::*;
use predicates::prelude::*;
use std::process::Command;
use tempfile::NamedTempFile;
use serde_json::Value;
use std::fs;

#[test]
fn test_full_ledger_flow() -> Result<(), Box<dyn std::error::Error>> {
    let db_file = NamedTempFile::new()?;
    let db_path = db_file.path().to_str().unwrap();
    let backup_file = NamedTempFile::new()?;
    let backup_path = backup_file.path().to_str().unwrap();
    let restored_db_file = NamedTempFile::new()?;
    let restored_db_path = restored_db_file.path().to_str().unwrap();
    let key = "test_key";

    // 1. Initialize the database
    let mut cmd = Command::cargo_bin("cli")?;
    cmd.arg("init-db").arg("--db-path").arg(db_path).arg("--encryption-key").arg(key);
    cmd.assert().success().stdout(predicate::str::contains("Database initialized successfully"));

    // 2. Add some transactions
    let mut cmd = Command::cargo_bin("cli")?;
    cmd.arg("add")
        .arg("--db-path").arg(db_path).arg("--encryption-key").arg(key)
        .arg("--person").arg("Alice").arg("--amount").arg("100").arg("--date").arg("2025-01-01");
    cmd.assert().success().stdout(predicate::str::contains("Transaction added successfully"));

    let mut cmd = Command::cargo_bin("cli")?;
    cmd.arg("add")
        .arg("--db-path").arg(db_path).arg("--encryption-key").arg(key)
        .arg("--person").arg("Bob").arg("--amount").arg("200").arg("--date").arg("2025-01-02");
    cmd.assert().success().stdout(predicate::str::contains("Transaction added successfully"));

    let mut cmd = Command::cargo_bin("cli")?;
    cmd.arg("add")
        .arg("--db-path").arg(db_path).arg("--encryption-key").arg(key)
        .arg("--person").arg("Alice").arg("--amount").arg("-50").arg("--date").arg("2025-01-03");
    cmd.assert().success().stdout(predicate::str::contains("Transaction added successfully"));

    // 3. List all transactions and verify
    let mut cmd = Command::cargo_bin("cli")?;
    cmd.arg("list").arg("--db-path").arg(db_path).arg("--encryption-key").arg(key);
    let output = cmd.assert().success().stdout(predicate::str::is_empty().not()).get_output().stdout.clone();
    let transactions: Value = serde_json::from_slice(&output)?;
    assert!(transactions.is_array());
    assert_eq!(transactions.as_array().unwrap().len(), 3);

    // 4. Check balances and verify
    let mut cmd = Command::cargo_bin("cli")?;
    cmd.arg("balances").arg("--db-path").arg(db_path).arg("--encryption-key").arg(key);
    let output = cmd.assert().success().stdout(predicate::str::is_empty().not()).get_output().stdout.clone();
    let balances: Value = serde_json::from_slice(&output)?;
    assert!(balances.is_array());
    assert_eq!(balances.as_array().unwrap().len(), 2);
    let alice_balance = balances.as_array().unwrap().iter().find(|b| b["person"] == "Alice").unwrap();
    assert_eq!(alice_balance["balance"], 50);

    let mut cmd = Command::cargo_bin("cli")?;
    cmd.arg("balance").arg("--db-path").arg(db_path).arg("--encryption-key").arg(key)
        .arg("--person").arg("Alice");
    cmd.assert().success().stdout(predicate::str::contains("50"));

    // 5. Backup the database
    let mut cmd = Command::cargo_bin("cli")?;
    cmd.arg("backup-db").arg("--db-path").arg(db_path).arg("--encryption-key").arg(key)
        .arg("--backup-path").arg(backup_path);
    cmd.assert().success().stdout(predicate::str::contains("Backup successful"));
    assert!(fs::metadata(backup_path)?.len() > 0);

    // 6. Restore the database to a new path
    let mut cmd = Command::cargo_bin("cli")?;
    cmd.arg("restore-db").arg("--backup-path").arg(backup_path).arg("--encryption-key").arg(key)
        .arg("--db-path").arg(restored_db_path);
    cmd.assert().success().stdout(predicate::str::contains("Restore successful"));

    // 7. Verify the restored database
    let mut cmd = Command::cargo_bin("cli")?;
    cmd.arg("balance").arg("--db-path").arg(restored_db_path).arg("--encryption-key").arg(key)
        .arg("--person").arg("Alice");
    cmd.assert().success().stdout(predicate::str::contains("50"));

    // 8. Test opening with wrong key
    let mut cmd = Command::cargo_bin("cli")?;
    cmd.arg("open-db").arg("--db-path").arg(db_path).arg("--encryption-key").arg("wrong_key");
    cmd.assert().failure().stderr(predicate::str::contains("Failed to open database"));

    Ok(())
}
