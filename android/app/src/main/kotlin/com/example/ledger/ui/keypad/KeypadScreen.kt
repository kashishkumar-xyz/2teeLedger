package com.example.ledger.ui.keypad

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
fun KeypadScreen(
    viewModel: KeypadViewModel = viewModel(),
    onNavigateToAddTransaction: (amount: String) -> Unit,
    onNavigateToAccounts: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val theme = uiState.theme

    val backgroundColor = if (theme == KeypadTheme.GREEN) GreenBackground else RedBackground
    val primaryColor = if (theme == KeypadTheme.GREEN) GreenPrimary else RedPrimary

    Box(modifier = Modifier.fillMaxSize()) {
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
                        ActionButtons(
                            onEvent = viewModel::onEvent,
                            theme = theme,
                            onSubmitClick = {
                                val amount = uiState.rawText
                                if (amount != "0") {
                                    val finalAmount =
                                        if (theme == KeypadTheme.RED) "-$amount" else amount
                                    onNavigateToAddTransaction(finalAmount)
                                    viewModel.onEvent(KeypadEvent.Reset)
                                }
                            }
                        )
                    }
                }

                Footer(theme = theme, onNavigateToAccounts = onNavigateToAccounts)
            }
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
fun Footer(theme: KeypadTheme, onNavigateToAccounts: () -> Unit) {
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
            FooterButton(icon = Icons.Default.History, label = "Recents", isSelected = false, theme = theme, onClick = {})
            FooterButton(icon = Icons.Default.Apps, label = "Keypad", isSelected = true, theme = theme, onClick = {})
            FooterButton(icon = Icons.Default.Person, label = "Accounts", isSelected = false, theme = theme, onClick = onNavigateToAccounts)
        }
    }
}

@Composable // recents, keypad, accounts
fun FooterButton(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    theme: KeypadTheme,
    onClick: () -> Unit
) {
    val primaryColor = if (theme == KeypadTheme.GREEN) GreenPrimary else RedPrimary
    val color = if (isSelected) primaryColor else Color.White.copy(alpha = 0.6f)
    Column(
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(bounded = false),
            onClick = onClick
        ),
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
                    ActionButtons(onEvent = {}, theme = KeypadTheme.GREEN, onSubmitClick = {})
                }
            }
            Footer(theme = KeypadTheme.GREEN, onNavigateToAccounts = {})
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

        Column(
            modifier = Modifier.pointerInput(theme) {
                detectHorizontalDragGestures { change, dragAmount ->
                    change.consume()
                    if (dragAmount < 0 && theme == KeypadTheme.RED) { // Swipe Left to Right (->)
                        onEvent(KeypadEvent.ThemeChange(KeypadTheme.GREEN))
                    } else if (dragAmount > 0 && theme == KeypadTheme.GREEN) { // Swipe Right to Left (<-)
                        onEvent(KeypadEvent.ThemeChange(KeypadTheme.RED))
                    }
                }
            },
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
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
private fun ActionButtons(
    onEvent: (KeypadEvent) -> Unit,
    theme: KeypadTheme,
    onSubmitClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ActionButton(text = "-", onClick = {
            if (theme == KeypadTheme.GREEN) {
                onEvent(KeypadEvent.ThemeChange(KeypadTheme.RED))
            }
        }, modifier = Modifier.weight(1f), isHighlighted = theme == KeypadTheme.RED, theme = theme)
        ActionButton(
            text = "Submit",
            onClick = onSubmitClick,
            modifier = Modifier.weight(1f),
            isSubmit = true,
            isHighlighted = true,
            theme = theme
        )
        ActionButton(text = "+", onClick = {
            if (theme == KeypadTheme.RED) {
                onEvent(KeypadEvent.ThemeChange(KeypadTheme.GREEN))
            }
        }, modifier = Modifier.weight(1f), isHighlighted = theme == KeypadTheme.GREEN, theme = theme)
    }
}

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSubmit: Boolean = false,
    isHighlighted: Boolean,
    theme: KeypadTheme
) {
    val primaryColor = if (theme == KeypadTheme.GREEN) GreenPrimary else RedPrimary
    Button(
        onClick = onClick,
        modifier = modifier.height(if (isSubmit) 64.dp else 56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isHighlighted) primaryColor else primaryColor.copy(alpha = 0.1f),
            contentColor = if (isHighlighted) Color.Black else Color.White
        )
    ) {
        Text(text = text, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun KeypadButton(
    text: String,
    onEvent: (KeypadEvent) -> Unit,
    modifier: Modifier = Modifier,
    theme: KeypadTheme
) {
    val primaryColor = if (theme == KeypadTheme.GREEN) GreenPrimary else RedPrimary

    Surface(
        modifier = modifier
            .padding(4.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { _ ->
                        when (text) {
                            "C" -> onEvent(KeypadEvent.Clear)
                            "backspace" -> onEvent(KeypadEvent.Backspace)
                            else -> onEvent(KeypadEvent.Number(text.toInt()))
                        }
                    }
                )
            },
        shape = CircleShape,
        color = Color.Transparent,
        border = BorderStroke(2.dp, primaryColor.copy(alpha = 0.2f))
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (text == "backspace") {
                Icon(
                    imageVector = Icons.Default.Backspace,
                    contentDescription = "Backspace",
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
            } else {
                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}
