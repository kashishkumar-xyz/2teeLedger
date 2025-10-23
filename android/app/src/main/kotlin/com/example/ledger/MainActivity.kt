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
import com.example.ledger.ui.keypad.KeypadScreen
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
                    NavHost(navController = navController, startDestination = "amount-keypad") {
                        composable("amount-keypad") {
                            KeypadScreen(
                                onNavigateToAddTransaction = { amount ->
                                    navController.navigate("addTransaction?amount=$amount")
                                },
                                onNavigateToAccounts = {
                                    navController.navigate("balances")
                                }
                            )
                        }
                        composable("balances") {
                            BalanceScreen(
                                viewModel = balanceViewModel,
                                onNavigateToHistory = { personName ->
                                    navController.navigate("history/$personName")
                                },
                                onNavigateToAddTransaction = {
                                    navController.navigate("addTransaction")
                                }
                            )
                        }
                        composable(
                            "addTransaction?amount={amount}&person={person}",
                            arguments = listOf(
                                navArgument("amount") {
                                    type = NavType.StringType
                                    nullable = true
                                },
                                navArgument("person") {
                                    type = NavType.StringType
                                    nullable = true
                                }
                            )
                        ) {
                            AddTransactionScreen(
                                initialAmount = it.arguments?.getString("amount"),
                                initialPerson = it.arguments?.getString("person"),
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
                            TransactionHistoryScreen(
                                personName = personName,
                                viewModel = historyViewModel,
                                onNavigateToAddTransaction = { person ->
                                    navController.navigate("addTransaction?person=$person")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
