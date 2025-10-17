package com.example.teeledger.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.teeledger.jni.LedgerCore
import com.example.teeledger.models.BalanceSummary
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData

class LedgerViewModel : ViewModel() {

    // This should be initialized properly in a real app
    private val dbPath = "/data/data/com.example.teeledger/files/ledger.db"
    private val encryptionKey = "a_very_secret_key"

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getBalances() = liveData {
        _isLoading.postValue(true) // Set loading to true
        try {
            val jsonResult = LedgerCore.list_balances(dbPath, encryptionKey)
            // Assuming that if jsonResult starts with "Error:", it's an error message
            if (jsonResult.startsWith("Error:")) {
                _errorMessage.postValue(jsonResult)
                emit(emptyList()) // Emit empty list on error
            } else {
                val type = object : TypeToken<List<BalanceSummary>>() {}.type
                val balances = Gson().fromJson<List<BalanceSummary>>(jsonResult, type)
                emit(balances)
            }
        } catch (e: Exception) {
            _errorMessage.postValue("Failed to fetch balances: ${e.message}")
            emit(emptyList()) // Emit empty list on exception
        } finally {
            _isLoading.postValue(false) // Set loading to false
        }
    }
    
    fun getBalances_for_test(): List<BalanceSummary> {
        return emptyList()
    }
}
