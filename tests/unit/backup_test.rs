use ledger_lib::backup::backup_db;
use ledger_lib::db::{initialize_db, add_transaction, open_encrypted_db, EncryptionKey};
use rusqlite::Connection;
use tempfile::NamedTempFile;
use std::fs;

#[test]
fn test_backup_db() -> Result<(), Box<dyn std::error::Error>> {
    let db_file = NamedTempFile::new()?;
    let db_path = db_file.path().to_str().unwrap();
    let backup_file = NamedTempFile::new()?;
    let backup_path = backup_file.path().to_str().unwrap();
    let mut key = EncryptionKey("test_key".to_string());

    // 1. Create and initialize a source database
    let conn = Connection::open(db_path)?;
    conn.pragma_update(None, "key", &key.0)?;
    initialize_db(&conn)?;
    add_transaction(&conn, "Alice", 100, "2025-01-01", None)?;
    conn.close().map_err(|(_, e)| e)?;

    // 2. Perform the backup
    backup_db(db_path, backup_path)?;

    // 3. Verify the backup file exists and is not empty
    assert!(fs::metadata(backup_path)?.len() > 0);

    // 4. Try to open the backup file to ensure it's a valid SQLite database
    let backup_conn = open_encrypted_db(backup_path, &mut key)?;
    let count: i64 = backup_conn.query_row("SELECT COUNT(*) FROM transactions_history", [], |row| row.get(0))?;
    assert_eq!(count, 1);

    Ok(())
}
