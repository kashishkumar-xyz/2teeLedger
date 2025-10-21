package com.example.ledger.ui.keypad

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ledger.ui.keypad.theme.LedgerTheme

// Theme Colors
val GreenPrimary = Color(0xFF39E079)
val GreenBackground = Color(0xFF122017)
val RedPrimary = Color(0xFFEF4444)
val RedBackground = Color(0xFF201212)

@Composable
fun KeypadScreen(viewModel: KeypadViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val theme = uiState.theme

    val backgroundColor = if (theme == KeypadTheme.GREEN) GreenBackground else RedBackground
    val primaryColor = if (theme == KeypadTheme.GREEN) GreenPrimary else RedPrimary

    LedgerTheme(theme = uiState.theme) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .statusBarsPadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header()

            // Main Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Amount Display
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = primaryColor)) {
                                append("$")
                            }
                            withStyle(style = SpanStyle(color = primaryColor.copy(alpha = 0.9f))) {
                                append(uiState.displayText)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.weight(1f))

                // Grouping container for keypad and action buttons
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Keypad(onEvent = viewModel::onEvent, theme = theme)
                    ActionButtons(onEvent = viewModel::onEvent, theme = theme)
                }
            }

            Footer(theme = theme)
        }
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(48.dp))
        IconButton(onClick = { /* TODO */ }) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun Footer(theme: KeypadTheme) {
    val primaryColor = if (theme == KeypadTheme.GREEN) GreenPrimary else RedPrimary
    Column {
        Divider(color = primaryColor.copy(alpha = 0.2f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FooterButton(icon = Icons.Default.History, label = "Recents", isSelected = false, theme = theme)
            FooterButton(icon = Icons.Default.Apps, label = "Keypad", isSelected = true, theme = theme)
            FooterButton(icon = Icons.Default.Person, label = "Accounts", isSelected = false, theme = theme)
        }
    }
}

@Composable // recents, keypad, accounts
fun FooterButton(icon: ImageVector, label: String, isSelected: Boolean, theme: KeypadTheme) {
    val primaryColor = if (theme == KeypadTheme.GREEN) GreenPrimary else RedPrimary
    val color = if (isSelected) primaryColor else Color.White.copy(alpha = 0.6f)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = color)
        Text(text = label, color = color, fontSize = 12.sp)
    }
}


@Preview(showBackground = true)
@Composable
fun KeypadScreenPreview() {
    LedgerTheme(theme = KeypadTheme.GREEN) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(GreenBackground)
                .statusBarsPadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header()
            // Main Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                // Amount Display
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                    ) {
                    Text(
                        text = "$0",
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Keypad(onEvent = {}, theme = KeypadTheme.GREEN)
                    ActionButtons(onEvent = {}, theme = KeypadTheme.GREEN)
                }
            }
            Footer(theme = KeypadTheme.GREEN)
        }
    }
}

@Composable
private fun Keypad(onEvent: (KeypadEvent) -> Unit, theme: KeypadTheme) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val buttons = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf("C", "0", "backspace")
        )

        val buttonSize: Dp = (maxWidth / 4).coerceAtMost(90.dp)
        val spacing: Dp = (buttonSize / 5).coerceAtMost(16.dp)

        Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
            buttons.forEach { rowButtons ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    rowButtons.forEach { buttonText ->
                        KeypadButton(
                            text = buttonText,
                            onEvent = onEvent,
                            modifier = Modifier.size(buttonSize),
                            theme = theme
                        )
                    }
                }
            }
        }
    }
}
@Composable
private fun ActionButtons(onEvent: (KeypadEvent) -> Unit, theme: KeypadTheme) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ActionButton(text = "-", onClick = {
            val newTheme = if (theme == KeypadTheme.GREEN) KeypadTheme.RED else KeypadTheme.GREEN
            onEvent(KeypadEvent.ThemeChange(newTheme))
        }, modifier = Modifier.weight(1f), theme = theme)
        ActionButton(text = "Submit", onClick = { /*TODO*/ }, modifier = Modifier.weight(1f), isSubmit = true, theme = theme)
        ActionButton(text = "+", onClick = { /*TODO*/ }, modifier = Modifier.weight(1f), isSubmit = true, theme = theme)
    }
}

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSubmit: Boolean = false,
    theme: KeypadTheme
) {
    val primaryColor = if (theme == KeypadTheme.GREEN) GreenPrimary else RedPrimary
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSubmit) Color.Transparent else primaryColor.copy(alpha = 0.1f),
            contentColor = primaryColor
        ),
        border = if (isSubmit) BorderStroke(2.dp, primaryColor) else null
    ) {
        Text(text = text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF122017)
@Composable
fun KeypadPreview() {
    Keypad(onEvent = {}, theme = KeypadTheme.GREEN)
}

@Composable
private fun KeypadButton(
    text: String,
    onEvent: (KeypadEvent) -> Unit,
    modifier: Modifier = Modifier,
    theme: KeypadTheme
) {
    val primaryColor = if (theme == KeypadTheme.GREEN) GreenPrimary else RedPrimary
    BoxWithConstraints(modifier = modifier) {
        val iconSize = maxWidth * 0.4f
        val numberFontSize = (maxWidth.value * 0.5f).sp
        val cFontSize = (maxWidth.value * 0.4f).sp

        val shape = when (text) {
            "C", "0", "backspace" -> RoundedCornerShape(50)
            else -> CircleShape
        }

        Button(
            onClick = {
                when (text) {
             //     "C" -> onEvent(KeypadEvent.Clear)
                    "backspace" -> onEvent(KeypadEvent.Backspace)
                    else -> onEvent(KeypadEvent.Number(text.toInt()))
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            if (text == "backspace") {
                                onEvent(KeypadEvent.Clear)
                            }
                        }
                    )
                },
            shape = shape,
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor.copy(alpha = 0.1f)),
            contentPadding = PaddingValues(0.dp)
        ) {
            if (text == "backspace") {
                Icon(
                    imageVector = Icons.Filled.Backspace,
                    contentDescription = "Backspace",
                    tint = primaryColor,
                    modifier = Modifier.size(iconSize)
                )
            } else {
                Text(
                    text,
                    fontSize = if (text == "C") cFontSize else numberFontSize,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )
            }
        }
    }
}


@Preview
@Composable
fun KeypadButtonPreviewNumber() {
    KeypadButton(text = "5", onEvent = {}, modifier = Modifier.size(80.dp), theme = KeypadTheme.GREEN)
}

@Preview
@Composable
fun KeypadButtonPreviewBackspace() {
    KeypadButton(text = "backspace", onEvent = {}, modifier = Modifier.size(80.dp), theme = KeypadTheme.GREEN)
}
