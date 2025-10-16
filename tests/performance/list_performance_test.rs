use ledger_lib::db::{add_transaction, initialize_db, list_transactions};
use rusqlite::Connection;
use std::time::Instant;

#[test]
fn test_list_performance() {
    let conn = Connection::open_in_memory().unwrap();
    initialize_db(&conn).unwrap();

    // Add 10,000 transactions
    for i in 0..10000 {
        let person = format!("Person {}", i % 100);
        let date = format!("2025-01-01");
        add_transaction(&conn, &person, 100, &date, None).unwrap();
    }

    // Test listing all transactions
    let start = Instant::now();
    let all_transactions = list_transactions(&conn, None, None, None).unwrap();
    let duration = start.elapsed();
    assert_eq!(all_transactions.len(), 10000);
    println!("List all transactions: {:?}", duration);
    assert!(duration.as_millis() < 100);

    // Test filtering by person
    let start = Instant::now();
    let person_transactions = list_transactions(&conn, Some("Person 1"), None, None).unwrap();
    let duration = start.elapsed();
    assert_eq!(person_transactions.len(), 100);
    println!("List transactions by person: {:?}", duration);
    assert!(duration.as_millis() < 100);

    // Test filtering by date
    let start = Instant::now();
    let date_transactions = list_transactions(&conn, None, Some("2025-01-01"), None).unwrap();
    let duration = start.elapsed();
    assert_eq!(date_transactions.len(), 10000);
    println!("List transactions by date: {:?}", duration);
    assert!(duration.as_millis() < 100);
}
