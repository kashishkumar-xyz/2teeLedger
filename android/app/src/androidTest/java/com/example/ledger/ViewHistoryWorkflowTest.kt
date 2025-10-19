package com.example.ledger

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class ViewHistoryWorkflowTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun viewHistory_workflow() {
        // First, add a transaction to ensure there's data to view
        composeTestRule.onNodeWithContentDescription("Add Transaction").performClick()
        composeTestRule.onNodeWithText("Person").performTextInput("History Test")
        composeTestRule.onNodeWithText("Amount").performTextInput("10.00")
        composeTestRule.onNodeWithText("Save Transaction").performClick()
        composeTestRule.waitForIdle()

        // Now on the balance screen, find the item and click it
        composeTestRule.onNodeWithText("History Test").assertIsDisplayed()
        composeTestRule.onNodeWithText("10.00").performClick()

        // Should be on the history screen
        composeTestRule.onNodeWithText("History for History Test").assertIsDisplayed()
        // And the transaction should be visible
        composeTestRule.onNodeWithText("10.00").assertIsDisplayed()
    }
}
