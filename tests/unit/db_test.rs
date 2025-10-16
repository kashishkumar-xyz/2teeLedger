use ledger::db::{initialize_db, add_transaction, list_transactions, list_balances, get_balance, open_encrypted_db};
use rusqlite::Connection;
use ledger::db::EncryptionKey;
use ledger::models::Balance;
use tempfile::NamedTempFile;

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

#[test]
fn test_list_transactions() {
    let conn = Connection::open_in_memory().unwrap();
    initialize_db(&conn).unwrap();

    // Add some transactions
    add_transaction(&conn, "Alice", 100, "2025-01-01", None).unwrap();
    add_transaction(&conn, "Bob", 200, "2025-01-02", Some("Lunch")).unwrap();
    add_transaction(&conn, "Alice", 150, "2025-01-03", Some("Dinner")).unwrap();

    // Test listing all transactions
    let all_transactions = list_transactions(&conn, None, None, None).unwrap();
    assert_eq!(all_transactions.len(), 3);
    assert_eq!(all_transactions[0].person, "Alice"); // Ordered by date DESC
    assert_eq!(all_transactions[1].person, "Bob");
    assert_eq!(all_transactions[2].person, "Alice");

    // Test filtering by person
    let alice_transactions = list_transactions(&conn, Some("Alice"), None, None).unwrap();
    assert_eq!(alice_transactions.len(), 2);
    assert_eq!(alice_transactions[0].person, "Alice");
    assert_eq!(alice_transactions[1].person, "Alice");

    // Test filtering by since_date
    let recent_transactions = list_transactions(&conn, None, Some("2025-01-02"), None).unwrap();
    assert_eq!(recent_transactions.len(), 2);
    assert_eq!(recent_transactions[0].date, "2025-01-03");
    assert_eq!(recent_transactions[1].date, "2025-01-02");

    // Test limiting results
    let limited_transactions = list_transactions(&conn, None, None, Some(1)).unwrap();
    assert_eq!(limited_transactions.len(), 1);
    assert_eq!(limited_transactions[0].person, "Alice");

    // Test combined filters
    let filtered_limited = list_transactions(&conn, Some("Alice"), Some("2025-01-02"), Some(1)).unwrap();
    assert_eq!(filtered_limited.len(), 1);
    assert_eq!(filtered_limited[0].person, "Alice");
    assert_eq!(filtered_limited[0].date, "2025-01-03");
}

#[test]
fn test_list_balances() {
    let conn = Connection::open_in_memory().unwrap();
    initialize_db(&conn).unwrap();

    // Add some transactions
    add_transaction(&conn, "Alice", 100, "2025-01-01", None).unwrap();
    add_transaction(&conn, "Bob", 200, "2025-01-02", Some("Lunch")).unwrap();
    add_transaction(&conn, "Alice", -50, "2025-01-03", Some("Coffee")).unwrap();
    add_transaction(&conn, "Bob", -150, "2025-01-04", Some("Dinner")).unwrap();
    add_transaction(&conn, "Charlie", 300, "2025-01-05", None).unwrap();

    let balances = list_balances(&conn).unwrap();

    let mut expected_balances = vec![
        Balance { person: "Alice".to_string(), balance: 50 },
        Balance { person: "Bob".to_string(), balance: 50 },
        Balance { person: "Charlie".to_string(), balance: 300 },
    ];
    expected_balances.sort_by(|a, b| a.person.cmp(&b.person));

    let mut actual_balances = balances;
    actual_balances.sort_by(|a, b| a.person.cmp(&b.person));

    assert_eq!(actual_balances, expected_balances);
}

#[test]
fn test_get_balance() {
    let conn = Connection::open_in_memory().unwrap();
    initialize_db(&conn).unwrap();

    // Add some transactions
    add_transaction(&conn, "Alice", 100, "2025-01-01", None).unwrap();
    add_transaction(&conn, "Bob", 200, "2025-01-02", Some("Lunch")).unwrap();
    add_transaction(&conn, "Alice", -50, "2025-01-03", Some("Coffee")).unwrap();

    let alice_balance = get_balance(&conn, "Alice").unwrap();
    assert_eq!(alice_balance, 50);

    let bob_balance = get_balance(&conn, "Bob").unwrap();
    assert_eq!(bob_balance, 200);

    let charlie_balance = get_balance(&conn, "Charlie").unwrap();
    assert_eq!(charlie_balance, 0);
}

#[test]
fn test_open_encrypted_db() -> Result<(), Box<dyn std::error::Error>> {
    let db_file = NamedTempFile::new()?;
    let db_path = db_file.path().to_str().unwrap();
    let mut key = EncryptionKey("test_key".to_string());
    let mut wrong_key = EncryptionKey("wrong_key".to_string());

    // 1. Open and initialize with correct key
    let conn = open_encrypted_db(db_path, &mut key)?;
    initialize_db(&conn)?;
    conn.close().map_err(|(_, e)| e)?;

    // 2. Open with correct key again
    let conn_ok = open_encrypted_db(db_path, &mut key);
    assert!(conn_ok.is_ok());

    // 3. Open with incorrect key
    let conn_err = open_encrypted_db(db_path, &mut wrong_key);
    assert!(conn_err.is_err());

    Ok(())
}
