package com.example.ledger.viewmodel

import com.example.ledger.ffi.LedgerRepository
import com.example.ledger.model.Balance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class BalanceViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var ledgerRepository: LedgerRepository
    private lateinit var viewModel: BalanceViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        ledgerRepository = mock()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads balances successfully`() = runTest {
        // Given
        val expectedBalances = listOf(Balance("Alice", 100.0), Balance("Bob", -50.0))
        whenever(ledgerRepository.getAllBalances()).thenReturn(expectedBalances)

        // When
        viewModel = BalanceViewModel(ledgerRepository)

        // Then
        testDispatcher.scheduler.advanceUntilIdle() // Execute coroutines
        assertEquals(expectedBalances, viewModel.balances.value)
        assertEquals(null, viewModel.error.value)
    }

    @Test
    fun `init handles error when loading balances`() = runTest {
        // Given
        val errorMessage = "Database error"
        whenever(ledgerRepository.getAllBalances()).thenThrow(RuntimeException(errorMessage))

        // When
        viewModel = BalanceViewModel(ledgerRepository)

        // Then
        testDispatcher.scheduler.advanceUntilIdle() // Execute coroutines
        assertEquals(emptyList<Balance>(), viewModel.balances.value)
        assertEquals("Failed to load balances: $errorMessage", viewModel.error.value)
    }
}
