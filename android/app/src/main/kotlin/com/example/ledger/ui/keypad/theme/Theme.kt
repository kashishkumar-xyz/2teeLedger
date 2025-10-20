package com.example.ledger.ui.keypad.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkGreenColorScheme = darkColorScheme(
    primary = CustomGreen,
    background = ForensicDark,
    surface = ForensicLight
)

private val DarkRedColorScheme = darkColorScheme(
    primary = Red,
    background = ForensicDark,
    surface = ForensicLight
)

@Composable
fun KeypadTheme(
    theme: KeypadTheme = KeypadTheme.GREEN,
    content: @Composable () -> Unit
) {
    val colorScheme = when (theme) {
        KeypadTheme.GREEN -> DarkGreenColorScheme
        KeypadTheme.RED -> DarkRedColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}

enum class KeypadTheme {
    GREEN,
    RED
}