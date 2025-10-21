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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ledger.ffi.ILedgerRepository
import com.example.ledger.ui.keypad.KeypadScreen
import com.example.ledger.ui.search.SearchScreen
import com.example.ledger.viewmodel.BalanceViewModel
import com.example.ledger.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup Repository and ViewModels
        val ledgerRepository: ILedgerRepository = LedgerRepository(applicationContext)
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
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "keypad_screen") {
                        composable("keypad_screen") {
                            KeypadScreen(onNavigateToSearch = { navController.navigate("search_screen") })
                        }
                        composable("search_screen") {
                            SearchScreen(onNavigateToAddContact = { /* TODO: Implement navigation to Add New Contact */ })
                        }
                    }
                }
            }
        }
    }
}
