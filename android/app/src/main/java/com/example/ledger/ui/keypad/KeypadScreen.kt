package com.example.ledger.ui.keypad

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

// ... (rest of the imports)

@Composable
fun KeypadScreen(viewModel: KeypadViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.padding(16.dp).semantics { contentDescription = "Keypad Screen ${uiState.theme}" }
    ) {
        Text(text = uiState.displayedValue, modifier = Modifier.fillMaxWidth().testTag("display"))

        Keypad(onNumberClick = { viewModel.onNumberPress(it) },
            onBackspaceClick = { viewModel.onBackspacePress() },
            onClear = { viewModel.onClear() })
    }
}

@Composable
private fun Keypad(
    onNumberClick: (Int) -> Unit,
    onBackspaceClick: () -> Unit,
    onClear: () -> Unit
) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            NumberButton(number = 7, onClick = onNumberClick)
            NumberButton(number = 8, onClick = onNumberClick)
            NumberButton(number = 9, onClick = onNumberClick)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            NumberButton(number = 4, onClick = onNumberClick)
            NumberButton(number = 5, onClick = onNumberClick)
            NumberButton(number = 6, onClick = onNumberClick)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            NumberButton(number = 1, onClick = onNumberClick)
            NumberButton(number = 2, onClick = onNumberClick)
            NumberButton(number = 3, onClick = onNumberClick)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            NumberButton(number = 0, onClick = onNumberClick)
            Box(modifier = Modifier.pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onBackspaceClick() },
                    onLongPress = { onClear() }
                )
            }) {
                Text(text = "<")
            }
        }
    }
}

@Composable
private fun NumberButton(number: Int, onClick: (Int) -> Unit) {
    Button(onClick = { onClick(number) }) {
        Text(text = number.toString())
    }
}
