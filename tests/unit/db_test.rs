use ledger::db::{initialize_db, add_transaction};
use rusqlite::Connection;

#[test]
fn test_add_transaction() {
    let conn = Connection::open_in_memory().unwrap();
    initialize_db(&conn).unwrap();

    let result = add_transaction(&conn, "John Doe", 1000, "2025-10-26", Some("Test"));
    assert!(result.is_ok());

    let mut stmt = conn.prepare("SELECT COUNT(*) FROM transactions_history").unwrap();
    let count: i64 = stmt.query_row([], |row| row.get(0)).unwrap();
    assert_eq!(count, 1);
}
