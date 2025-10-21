package com.example.ledger.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ledger.ffi.LedgerRepository
import com.example.ledger.ffi.ILedgerRepository
import com.example.ledger.model.Balance
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SearchViewModel(private val ledgerRepository: ILedgerRepository) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _allBalances = MutableStateFlow<List<Balance>>(emptyList())

    private val _searchResults = MutableStateFlow<List<Balance>>(emptyList())
    val searchResults: StateFlow<List<Balance>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        @OptIn(FlowPreview::class)
        _searchText
            .debounce(300L) // Debounce search input
            .distinctUntilChanged() // Only emit when value changes
            .onEach { query ->
                _searchResults.value = if (query.isBlank()) {
                    _allBalances.value // Show all if query is empty
                } else {
                    _allBalances.value.filter { balance ->
                        balance.person.contains(query, ignoreCase = true)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onSearchTextChanged(text: String) {
        _searchText.value = text
    }

    fun loadAllBalances() = viewModelScope.launch {
        _isLoading.value = true
        _error.value = null
        try {
            _allBalances.value = ledgerRepository.getAllBalances()
            // Trigger search filter immediately after loading all balances
            _searchResults.value = if (_searchText.value.isBlank()) {
                _allBalances.value
            } else {
                _allBalances.value.filter { balance ->
                    balance.person.contains(_searchText.value, ignoreCase = true)
                }
            }
        } catch (e: Exception) {
            _error.value = e.message
        } finally {
            _isLoading.value = false
        }
    }
}
