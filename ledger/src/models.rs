use serde::{Deserialize, Serialize};

/// Represents a transaction.
#[derive(Debug, PartialEq, Serialize, Deserialize)]
pub struct Transaction {
    /// The unique identifier of the transaction.
    pub id: i64,
    /// The date of the transaction.
    pub date: String,
    /// The person associated with the transaction.
    pub person: String,
    /// The amount of the transaction.
    pub amount: i64,
    /// An optional note for the transaction.
    pub note: Option<String>,
}

/// Represents a balance.
#[derive(Debug, PartialEq, Serialize, Deserialize)]
pub struct Balance {
    /// The person associated with the balance.
    pub person: String,
    /// The total balance for the person.
    pub balance: i64,
}
