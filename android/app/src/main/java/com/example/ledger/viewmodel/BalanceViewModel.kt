package com.example.ledger.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ledger.ffi.LedgerRepository
import com.example.ledger.model.Balance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BalanceViewModel(private val ledgerRepository: LedgerRepository) : ViewModel() {

    private val _balances = MutableStateFlow<List<Balance>>(emptyList())
    val balances: StateFlow<List<Balance>> = _balances

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadBalances() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _balances.value = ledgerRepository.getAllBalances()
            } catch (e: Exception) {
                _error.value = "Failed to load balances: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveTransaction(person: String, amount: Double, note: String?) {
        viewModelScope.launch {
            // Don't set loading for save, it's a quick operation and loadBalances will handle it
            try {
                ledgerRepository.addTransaction(person, amount, note)
                // Refresh the balance list after a successful transaction
                loadBalances()
            } catch (e: Exception) {
                _error.value = "Failed to save transaction: ${e.message}"
            }
        }
    }
}