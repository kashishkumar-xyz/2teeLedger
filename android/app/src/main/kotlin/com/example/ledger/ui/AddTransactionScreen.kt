package com.example.ledger.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ledger.viewmodel.BalanceViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    viewModel: BalanceViewModel,
    initialAmount: String?,
    initialPerson: String?,
    onTransactionSaved: (person: String, amount: Double, note: String?) -> Unit
) {
    val balances by viewModel.balances.collectAsState()
    val personList = balances.map { it.person }.distinct()

    var person by remember { mutableStateOf(initialPerson ?: "") }
    var amount by remember { mutableStateOf(initialAmount ?: "") }
    var note by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var personInputError by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add Transaction") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {
                OutlinedTextField(
                    value = person,
                    onValueChange = { newPersonValue ->
                        val invalidCharsRegex = Regex("[0-9!@#\$%^&*(),.?\":{}|<>]")
                        val sanitizedValue = newPersonValue.replace(invalidCharsRegex, "")

                        personInputError = newPersonValue.length != sanitizedValue.length

                        val formattedPerson = sanitizedValue.split(' ').joinToString(" ") {
                            if (it.isNotEmpty()) {
                                it.lowercase(Locale.getDefault()).replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.getDefault()
                                    ) else it.toString()
                                }
                            } else {
                                ""
                            }
                        }
                        person = formattedPerson

                        val filtered = personList.filter { it.contains(formattedPerson, ignoreCase = true) }
                        expanded = filtered.isNotEmpty()
                    },
                    label = { Text("Person") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    isError = (isError && person.isBlank()) || personInputError,
                    supportingText = {
                        if (personInputError) {
                            Text("special characters not allowed")
                        }
                    }
                )
                val filteredOptions = personList.filter { it.contains(person, ignoreCase = true) }
                if (filteredOptions.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        filteredOptions.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    person = selectionOption
                                    expanded = false
                                },
                            )
                        }
                    }
                }
            }
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = isError && amount.toDoubleOrNull() == null
            )
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note (Optional)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val amountDouble = amount.toDoubleOrNull()
                    val formattedPerson = person.trim()
                    if (formattedPerson.isNotBlank() && amountDouble != null) {
                        val finalNote = note.takeIf { it.isNotBlank() }
                        onTransactionSaved(formattedPerson, amountDouble, finalNote)
                    } else {
                        isError = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Transaction")
            }
        }
    }
}
