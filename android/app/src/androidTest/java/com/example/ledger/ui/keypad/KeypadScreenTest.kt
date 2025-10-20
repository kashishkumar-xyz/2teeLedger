package com.example.ledger.ui.keypad

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.longClick
import com.example.ledger.ui.keypad.theme.KeypadTheme
import org.junit.Rule
import org.junit.Test

class KeypadScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenNumberButtonClicked_displayIsUpdated() {
        composeTestRule.setContent {
            KeypadTheme {
                KeypadScreen()
            }
        }

        composeTestRule.onNodeWithText("7").performClick()
        composeTestRule.onNodeWithText("5").performClick()

        composeTestRule.onNodeWithTag("display").assert(hasText("75"))
    }

    @Test
    fun whenBackspaceClicked_thenLastDigitRemoved() {
        composeTestRule.setContent {
            KeypadTheme {
                KeypadScreen()
            }
        }

        composeTestRule.onNodeWithText("7").performClick()
        composeTestRule.onNodeWithText("5").performClick()
        composeTestRule.onNodeWithText("<").performClick()

        composeTestRule.onNodeWithTag("display").assert(hasText("7"))
    }

    @Test
    fun whenBackspaceLongClicked_thenDisplayCleared() {
        composeTestRule.setContent {
            KeypadTheme {
                KeypadScreen()
            }
        }

        composeTestRule.onNodeWithText("7").performClick()
        composeTestRule.onNodeWithText("5").performClick()
        composeTestRule.onNodeWithText("<").performTouchInput { longClick() }

        composeTestRule.onNodeWithTag("display").assert(hasText("0"))
    }

    @Test
    fun whenThemeIsGreen_thenGreenContentDescriptionExists() {
        composeTestRule.setContent {
            KeypadTheme(theme = com.example.ledger.ui.keypad.theme.KeypadTheme.GREEN) {
                KeypadScreen()
            }
        }

        composeTestRule.onNodeWithContentDescription("Keypad Screen GREEN").assertExists()
    }

    @Test
    fun whenThemeIsRed_thenRedContentDescriptionExists() {
        composeTestRule.setContent {
            KeypadTheme(theme = com.example.ledger.ui.keypad.theme.KeypadTheme.RED) {
                KeypadScreen()
            }
        }

        composeTestRule.onNodeWithContentDescription("Keypad Screen RED").assertExists()
    }
}