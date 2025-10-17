package com.example.teeledger.viewmodel

import androidx.lifecycle.ViewModel
import com.example.teeledger.jni.LedgerCore
import java.text.SimpleDateFormat
import java.util.* 

class AddTransactionViewModel : ViewModel() {

    // This should be initialized properly in a real app
    private val dbPath = "/data/data/com.example.teeledger/files/ledger.db"
    private val encryptionKey = "a_very_secret_key"

    fun addTransaction(person: String, amount: Long, note: String?): Boolean {
        if (person.isBlank() || amount == 0L) {
            return false
        }

        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val result = LedgerCore.add_transaction(dbPath, encryptionKey, person, amount, date, note)
        return result.contains("success", ignoreCase = true)
    }
}
