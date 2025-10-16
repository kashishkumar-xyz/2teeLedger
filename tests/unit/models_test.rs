use ledger_lib::models::{Balance, Transaction};

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

#[test]
fn test_balance_serialization() {
    let balance = Balance {
        person: "Alice".to_string(),
        balance: 500,
    };

    let serialized = serde_json::to_string(&balance).unwrap();
    let deserialized: Balance = serde_json::from_str(&serialized).unwrap();

    assert_eq!(balance, deserialized);
}
