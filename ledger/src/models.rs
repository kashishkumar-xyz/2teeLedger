use serde::{Deserialize, Serialize};

#[derive(Debug, PartialEq, Serialize, Deserialize)]
pub struct Transaction {
    pub id: i64,
    pub date: String,
    pub person: String,
    pub amount: i64,
    pub note: Option<String>,
}

#[derive(Debug, PartialEq, Serialize, Deserialize)]
pub struct Balance {
    pub person: String,
    pub balance: i64,
}
