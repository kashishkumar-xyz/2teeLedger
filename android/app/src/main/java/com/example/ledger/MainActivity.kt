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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ledger.ffi.LedgerRepository
import com.example.ledger.security.KeyManager
import com.example.ledger.ui.AddTransactionScreen
import com.example.ledger.ui.BalanceScreen
import com.example.ledger.ui.TransactionHistoryScreen
import com.example.ledger.viewmodel.BalanceViewModel
import com.example.ledger.viewmodel.TransactionHistoryViewModel
import com.example.ledger.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup Repository and ViewModels
        val ledgerRepository = LedgerRepository(applicationContext)
        val viewModelFactory = ViewModelFactory(ledgerRepository)
        val balanceViewModel: BalanceViewModel by viewModels { viewModelFactory }
        val historyViewModel: TransactionHistoryViewModel by viewModels { viewModelFactory }

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
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "balance") {
                        composable("balance") {
                            BalanceScreen(
                                viewModel = balanceViewModel,
                                onNavigateToHistory = { personName ->
                                    navController.navigate("history/$personName")
                                },
                                onNavigateToAddTransaction = { navController.navigate("addTransaction") }
                            )
                        }
                        composable("addTransaction") {
                            AddTransactionScreen(
                                onTransactionSaved = { person, amount, note ->
                                    balanceViewModel.saveTransaction(person, amount, note)
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable(
                            "history/{personName}",
                            arguments = listOf(navArgument("personName") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val personName = backStackEntry.arguments?.getString("personName") ?: ""
                            // The screen will call the viewmodel to load the data
                            TransactionHistoryScreen(
                                personName = personName,
                                viewModel = historyViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}