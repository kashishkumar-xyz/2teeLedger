package com.example.ledger.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ledger.ffi.LedgerRepository
import com.example.ledger.model.Balance
import com.example.ledger.ui.theme.LedgerTheme
import kotlinx.coroutines.runBlocking
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel = viewModel(), onNavigateToAddContact: () -> Unit = {}) {
    val searchText by viewModel.searchText.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        keyboardController?.show()
        viewModel.loadAllBalances() // Load all balances initially
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Balances") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF181A15),
                    titleContentColor = Color(0xFFEAEAEA)
                )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF181A15))
                .padding(it)
                .imePadding() // Adjusts for keyboard
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchText,
                onValueChange = { viewModel.onSearchTextChanged(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(Color(0xFF2C2F2A), RoundedCornerShape(24.dp)),
                placeholder = { Text("Search account holder", color = Color(0xFF888888), style = MaterialTheme.typography.labelLarge) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search icon", tint = Color(0xFFEAEAEA)) },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFFEAEAEA),
                    unfocusedTextColor = Color(0xFFEAEAEA),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color(0xFFEAEAEA)
                ),
                textStyle = MaterialTheme.typography.labelLarge
            )

            // Suggestions/Results Area Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Suggestions",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFFEAEAEA)
                )
                TextButton(
                    onClick = onNavigateToAddContact,
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFEAEAEA))
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add new contact", modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("New Contact")
                }
            }

            // Search Results
            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF3B82F6))
                }
            } else if (error != null) {
                Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    Text(text = "Error: $error", color = Color(0xFFF44336))
                }
            } else if (searchText.isEmpty() && searchResults.isEmpty()) {
                // Initial state with no search text and no results (e.g., if loadAllBalances failed or returned empty)
                Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    Text(text = "Start typing to find accounts.", color = Color(0xFF888888))
                }
            } else if (searchResults.isEmpty() && searchText.isNotEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    Text(text = "No results found", color = Color(0xFF888888))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(searchResults) { balance ->
                        AccountListItem(balance = balance)
                    }
                }
            }
        }
    }
}

@Composable
fun AccountListItem(balance: Balance) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2F2A))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder for Avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Gray, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = balance.person.first().uppercase(),
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = balance.person, style = MaterialTheme.typography.bodyMedium, color = Color(0xFFEAEAEA), maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(
                    text = String.format("$%.2f", balance.total),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (balance.total >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchScreen() {
    LedgerTheme {
        SearchScreen(viewModel = previewSearchViewModel(), onNavigateToAddContact = {})
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchScreenEmpty() {
    LedgerTheme {
        SearchScreen(viewModel = previewSearchViewModel(emptyList()), onNavigateToAddContact = {})
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchScreenNoResults() {
    LedgerTheme {
        SearchScreen(viewModel = previewSearchViewModel(emptyList(), "xyz"), onNavigateToAddContact = {})
    }
}

fun previewSearchViewModel(balances: List<Balance> = listOf(
    Balance("Avery Johnson", 240.0),
    Balance("Joanna Smith", -50.0),
    Balance("Michael Brown", 1200.50)
), searchText: String = "") : SearchViewModel {
    val mockRepo = mock<LedgerRepository>()
    runBlocking {
        whenever(mockRepo.getAllBalances()).thenReturn(balances)
    }
    val viewModel = SearchViewModel(mockRepo)
    viewModel.onSearchTextChanged(searchText)
    return viewModel
}
