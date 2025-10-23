package com.example.ledger.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ledger.model.Balance
import com.example.ledger.viewmodel.BalanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceScreen(
    viewModel: BalanceViewModel,
    onNavigateToHistory: (String) -> Unit,
    onNavigateToAddTransaction: () -> Unit
) {
    val balances by viewModel.balances.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ledger Balances")},
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddTransaction) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = error!!, color = Color.Red)
                }
            } else if (balances.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No balances found.")
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {                    items(balances) { balance ->
                        BalanceListItem(
                            balance = balance,
                            onClick = { onNavigateToHistory(balance.person) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BalanceListItem(balance: Balance, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2F2A)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = onClick
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = balance.person,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFEAEAEA)
            )
            Text(
                text = "$${String.format("%.2f", kotlin.math.abs(balance.total))}",
                color = if (balance.total >= 0) Color(0xFF4CAF50) else Color(0xFFF44336),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BalanceListItemPreview_Positive() {
    BalanceListItem(balance = Balance("Alice", 150.75), onClick = {})
}

@Preview(showBackground = true)
@Composable
fun BalanceListItemPreview_Negative() {
    BalanceListItem(balance = Balance("Bob", -25.50), onClick = {})
}
