package com.example.ledger

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class AddTransactionWorkflowTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun addTransaction_workflow() {
        // Start on BalanceScreen, find and click FAB
        composeTestRule.onNodeWithText("Ledger Balances").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Add Transaction").performClick()

        // Now on AddTransactionScreen
        composeTestRule.onNodeWithText("Add Transaction").assertIsDisplayed()

        // Fill out the form
        composeTestRule.onNodeWithText("Person").performTextInput("Test Person")
        composeTestRule.onNodeWithText("Amount").performTextInput("123.45")
        composeTestRule.onNodeWithText("Note (Optional)").performTextInput("UI Test")

        // Click save
        composeTestRule.onNodeWithText("Save Transaction").performClick()

        // Should be back on the balance screen
        composeTestRule.onNodeWithText("Ledger Balances").assertIsDisplayed()
        // And the new transaction should be reflected (eventually)
        composeTestRule.waitForIdle() // Wait for UI to update
        composeTestRule.onNodeWithText("Test Person").assertIsDisplayed()
        composeTestRule.onNodeWithText("123.45").assertIsDisplayed()
    }
}
