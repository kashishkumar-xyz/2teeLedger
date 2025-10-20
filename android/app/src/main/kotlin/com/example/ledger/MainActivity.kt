package com.example.ledger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.ledger.ffi.LedgerRepository
import com.example.ledger.security.KeyManager
import com.example.ledger.ui.keypad.KeypadScreen
import com.example.ledger.viewmodel.BalanceViewModel
import com.example.ledger.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup Repository and ViewModels
        val ledgerRepository = LedgerRepository(applicationContext)
        val viewModelFactory = ViewModelFactory(ledgerRepository)
        val balanceViewModel: BalanceViewModel by viewModels { viewModelFactory }

        // Initialize database and load data
        lifecycleScope.launch {
            val keyManager = KeyManager(applicationContext)
            val key = keyManager.getOrCreateDatabaseKey()
            try {
                ledgerRepository.openDatabase(key)
                balanceViewModel.loadBalances()
            } catch (e: Exception) {
                balanceViewModel.loadBalances()
            }
        }

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KeypadScreen()
                }
            }
        }
    }
}
