package com.example.ledger.ffi

import com.example.ledger.model.Balance
import com.example.ledger.model.Transaction

interface ILedgerRepository {
    suspend fun openDatabase(key: ByteArray)
    suspend fun addTransaction(person: String, amount: Double, note: String?)
    suspend fun getAllBalances(): List<Balance>
    suspend fun getTransactionsForPerson(person: String): List<Transaction>
}
