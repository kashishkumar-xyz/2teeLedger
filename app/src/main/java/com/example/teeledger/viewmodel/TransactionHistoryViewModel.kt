package com.example.teeledger.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.teeledger.jni.LedgerCore
import com.example.teeledger.models.TransactionDetails
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TransactionHistoryViewModel : ViewModel() {

    // This should be initialized properly in a real app
    private val dbPath = "/data/data/com.example.teeledger/files/ledger.db"
    private val encryptionKey = "a_very_secret_key"

    fun getTransactionHistory(person: String) = liveData {
        val jsonResult = LedgerCore.list_transactions(dbPath, encryptionKey, person, null, 0)
        val type = object : TypeToken<List<TransactionDetails>>() {}.type
        val transactions = Gson().fromJson<List<TransactionDetails>>(jsonResult, type)
        emit(transactions)
    }
    
    fun getTransactionHistory_for_test(person: String): List<TransactionDetails> {
        return emptyList()
    }
}
