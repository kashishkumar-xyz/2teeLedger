use rusqlite::{Connection, Result};

pub fn open_encrypted_db(db_path: &str, key: &str) -> Result<Connection> {
    let conn = Connection::open(db_path)?;
    conn.pragma_update(None, "key", &key)?;
    Ok(conn)
}
