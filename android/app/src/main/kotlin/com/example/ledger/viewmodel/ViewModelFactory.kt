package com.example.ledger.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ledger.ffi.ILedgerRepository
import com.example.ledger.ffi.LedgerRepository

class ViewModelFactory(private val ledgerRepository: ILedgerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BalanceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BalanceViewModel(ledgerRepository) as T
        }
        if (modelClass.isAssignableFrom(TransactionHistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionHistoryViewModel(ledgerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}