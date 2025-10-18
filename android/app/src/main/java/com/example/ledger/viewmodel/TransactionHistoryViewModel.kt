package com.example.ledger.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ledger.ffi.LedgerRepository
import com.example.ledger.model.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransactionHistoryViewModel(private val ledgerRepository: LedgerRepository) : ViewModel() {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadTransactionHistory(person: String) {
        viewModelScope.launch {
            try {
                _transactions.value = ledgerRepository.getTransactionsForPerson(person)
            } catch (e: Exception) {
                _error.value = "Failed to load transaction history: ${e.message}"
            }
        }
    }
}
