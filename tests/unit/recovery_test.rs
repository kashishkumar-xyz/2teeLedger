use ledger_lib::backup::backup_db;
use ledger_lib::db::{add_transaction, initialize_db, open_encrypted_db, EncryptionKey};
use ledger_lib::recovery::restore_db;
use rusqlite::Connection;
use tempfile::NamedTempFile;

#[test]
fn test_restore_db() -> Result<(), Box<dyn std::error::Error>> {
    let src_db_file = NamedTempFile::new()?;
    let src_db_path = src_db_file.path().to_str().unwrap();
    let backup_file = NamedTempFile::new()?;
    let backup_path = backup_file.path().to_str().unwrap();
    let dest_db_file = NamedTempFile::new()?;
    let dest_db_path = dest_db_file.path().to_str().unwrap();
    let mut key = EncryptionKey("test_key".to_string());

    // 1. Create and initialize a source database with some data
    let src_conn = Connection::open(src_db_path)?;
    src_conn.pragma_update(None, "key", &key.0)?;
    initialize_db(&src_conn)?;
    add_transaction(&src_conn, "Alice", 100, "2025-01-01", None)?;
    src_conn.close().map_err(|(_, e)| e)?;

    // 2. Backup the source database
    backup_db(src_db_path, backup_path)?;

    // 3. Restore the backup to a new destination database
    restore_db(backup_path, dest_db_path)?;

    // 4. Verify the restored database
    let dest_conn = open_encrypted_db(dest_db_path, &mut key, true)?;
    let count: i64 =
        dest_conn.query_row("SELECT COUNT(*) FROM transactions_history", [], |row| {
            row.get(0)
        })?;
    assert_eq!(count, 1);

    Ok(())
}
