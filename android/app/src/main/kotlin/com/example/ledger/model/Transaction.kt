package com.example.ledger.model

data class Transaction(
    val person: String,
    val amount: Double,
    val timestamp: Long,
    val note: String?
)
