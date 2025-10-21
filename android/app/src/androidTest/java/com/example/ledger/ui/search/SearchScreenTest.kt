package com.example.ledger.ui.search

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.ledger.ffi.LedgerRepository
import com.example.ledger.model.Balance
import com.example.ledger.ui.theme.LedgerTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockLedgerRepository: LedgerRepository
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        mockLedgerRepository = mock()
        // Mock the getAllBalances to return a predefined list
        runBlocking {
            whenever(mockLedgerRepository.getAllBalances()).thenReturn(listOf(
                Balance("Alice Smith", 100.0),
                Balance("Bob Johnson", -50.0),
                Balance("Charlie Brown", 200.0)
            ))
        }
        viewModel = SearchViewModel(mockLedgerRepository)

        composeTestRule.setContent {
            LedgerTheme {
                SearchScreen(viewModel = viewModel)
            }
        }
    }

    @Test
    fun search_bar_and_initial_elements_are_displayed() {
        composeTestRule.onNodeWithContentDescription("Search accounts").assertIsDisplayed()
        composeTestRule.onNodeWithText("Suggestions").assertIsDisplayed()
        composeTestRule.onNodeWithText("+ New Contact").assertIsDisplayed()
    }

    @Test
    fun typing_in_search_bar_filters_results() {
        composeTestRule.onNodeWithContentDescription("Search accounts").performTextInput("Alice")
        composeTestRule.onNodeWithText("Alice Smith").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bob Johnson").assertDoesNotExist()
    }

    @Test
    fun positive_balance_is_displayed_correctly() {
        composeTestRule.onNodeWithContentDescription("Search accounts").performTextInput("Alice")
        composeTestRule.onNodeWithText("$100.00").assertIsDisplayed()
    }

    @Test
    fun negative_balance_is_displayed_correctly() {
        composeTestRule.onNodeWithContentDescription("Search accounts").performTextInput("Bob")
        composeTestRule.onNodeWithText("-$50.00").assertIsDisplayed()
    }

    @Test
    fun no_results_message_is_displayed_when_no_matches() {
        composeTestRule.onNodeWithContentDescription("Search accounts").performTextInput("XYZ")
        composeTestRule.onNodeWithText("No results found").assertIsDisplayed()
    }

    @Test
    fun new_contact_button_navigates() {
        // This test can only verify the button is clickable and exists.
        // Actual navigation would be tested in an integration test with a navigation host.
        composeTestRule.onNodeWithText("+ New Contact").performClick()
        // Add verification for navigation if a mock navigator is passed to the screen
    }
}
