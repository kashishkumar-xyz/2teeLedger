package com.example.ledger.ui.keypad.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightGreenColorScheme = lightColorScheme(
    primary = Green,
    secondary = Green,
    tertiary = Green
)

private val LightRedColorScheme = lightColorScheme(
    primary = Red,
    secondary = Red,
    tertiary = Red
)

@Composable
fun KeypadTheme(
    darkTheme: Boolean = false, // for now we only support light theme
    theme: KeypadTheme = KeypadTheme.GREEN,
    content: @Composable () -> Unit
) {
    val colorScheme = when (theme) {
        KeypadTheme.GREEN -> LightGreenColorScheme
        KeypadTheme.RED -> LightRedColorScheme
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