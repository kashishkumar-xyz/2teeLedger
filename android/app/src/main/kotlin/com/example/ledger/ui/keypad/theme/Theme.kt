package com.example.ledger.ui.keypad.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.example.ledger.ui.keypad.KeypadTheme

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

val Typography = Typography()

@Composable
fun LedgerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    theme: KeypadTheme = KeypadTheme.GREEN,
    content: @Composable () -> Unit
) {
    val colorScheme = when (theme) {
        KeypadTheme.GREEN -> LightGreenColorScheme
        KeypadTheme.RED -> LightRedColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
