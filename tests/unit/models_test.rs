use ledger::models::Transaction;

#[test]
fn test_transaction_serialization() {
    let transaction = Transaction {
        id: 1,
        date: "2025-10-26".to_string(),
        person: "John Doe".to_string(),
        amount: 1000,
        note: Some("Test transaction".to_string()),
    };

    let serialized = serde_json::to_string(&transaction).unwrap();
    let deserialized: Transaction = serde_json::from_str(&serialized).unwrap();

    assert_eq!(transaction, deserialized);
}
