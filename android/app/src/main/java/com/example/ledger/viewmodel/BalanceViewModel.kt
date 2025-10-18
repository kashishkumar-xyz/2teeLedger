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


    fun loadBalances() {
        viewModelScope.launch {
            try {
                _balances.value = ledgerRepository.getAllBalances()
            } catch (e: Exception) {
                _error.value = "Failed to load balances: ${e.message}"
            }
        }
    }
}
