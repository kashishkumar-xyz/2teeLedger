use rusqlite::{Connection, Result};
use zeroize::Zeroize;

#[derive(Zeroize)]
#[zeroize(drop)]
pub struct EncryptionKey(pub String);

pub fn initialize_db(conn: &Connection) -> Result<()> {
    conn.execute(
        "CREATE TABLE IF NOT EXISTS transactions_history (
            tx_id TEXT NOT NULL,
            version INTEGER NOT NULL,
            data TEXT NOT NULL,
            op TEXT NOT NULL,
            created_at TEXT NOT NULL,
            PRIMARY KEY (tx_id, version)
        )",
        [],
    )?;
    Ok(())
}

pub fn open_encrypted_db(db_path: &str, key: &mut EncryptionKey) -> Result<Connection> {
    let conn = Connection::open(db_path)?;
    conn.pragma_update(None, "key", &key.0)?;
    conn.pragma_update(None, "journal_mode", "WAL")?;
    let integrity_check: String = conn.query_row("PRAGMA integrity_check", [], |row| row.get(0))?;
    if integrity_check != "ok" {
        // This is a simplified error handling. In a real application, you would
        // want to handle this more gracefully, perhaps by attempting a recovery.
        return Err(rusqlite::Error::ExecuteReturnedResults);
    }
    Ok(conn)
}