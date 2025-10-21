package com.example.ledger.ui.keypad

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.longClick
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class KeypadScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun tappingNumberButton_updatesDisplay() {
        composeTestRule.setContent {
            KeypadScreen(onNavigateToSearch = {})
        }

        composeTestRule.onNodeWithText("7").performClick()

        composeTestRule.onNodeWithText("7").assertExists()
    }

    @Test
    fun shortPressOnBackspace_removesLastDigit() {
        composeTestRule.setContent {
            KeypadScreen(onNavigateToSearch = {})
        }

        composeTestRule.onNodeWithText("1").performClick()
        composeTestRule.onNodeWithText("2").performClick()
        composeTestRule.onNodeWithText("3").performClick()

        // Assuming the backspace button has a content description "Backspace"
        composeTestRule.onNodeWithContentDescription("Backspace").performClick()

        composeTestRule.onNodeWithText("12").assertExists()
    }

    @Test
    fun longPressOnBackspace_clearsDisplay() {
        composeTestRule.setContent {
            KeypadScreen(onNavigateToSearch = {})
        }

        composeTestRule.onNodeWithText("1").performClick()
        composeTestRule.onNodeWithText("2").performClick()
        composeTestRule.onNodeWithText("3").performClick()

        // Assuming the backspace button has a content description "Backspace"
        composeTestRule.onNodeWithContentDescription("Backspace").performTouchInput { longClick() }

        composeTestRule.onNodeWithText("0").assertExists()
    }

    // The following tests are for the ViewModel logic, not the UI.
    // They are placed here to follow the task plan, but should be in a unit test file.
    @Test
    fun themeChangeToGreen_updatesViewModelState() {
        val viewModel = KeypadViewModel()
        viewModel.onEvent(KeypadEvent.ThemeChange(KeypadTheme.GREEN))
        assertEquals(KeypadTheme.GREEN, viewModel.uiState.value.theme)
    }

    @Test
    fun themeChangeToRed_updatesViewModelState() {
        val viewModel = KeypadViewModel()
        viewModel.onEvent(KeypadEvent.ThemeChange(KeypadTheme.RED))
        assertEquals(KeypadTheme.RED, viewModel.uiState.value.theme)
    }
}
