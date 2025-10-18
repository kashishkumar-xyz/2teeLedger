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
import com.example.ledger.ui.BalanceScreen
import com.example.ledger.viewmodel.BalanceViewModel
import com.example.ledger.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup Repository and ViewModel
        val ledgerRepository = LedgerRepository(applicationContext)
        val viewModelFactory = ViewModelFactory(ledgerRepository)
        val balanceViewModel: BalanceViewModel by viewModels { viewModelFactory }

        // Initialize database and load data
        lifecycleScope.launch {
            val keyManager = KeyManager(applicationContext)
            val key = keyManager.getOrCreateDatabaseKey()
            try {
                ledgerRepository.openDatabase(key)
                // Trigger the view model to load data now that the DB is open
                balanceViewModel.loadBalances()
            } catch (e: Exception) {
                // The ViewModel will catch and expose this error
                balanceViewModel.loadBalances()
            }
        }

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    BalanceScreen(viewModel = balanceViewModel)
                }
            }
        }
    }
}