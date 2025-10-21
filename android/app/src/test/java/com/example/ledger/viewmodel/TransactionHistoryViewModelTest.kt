package com.example.ledger.viewmodel

import com.example.ledger.ffi.ILedgerRepository
import com.example.ledger.ffi.LedgerRepository
import com.example.ledger.model.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
class TransactionHistoryViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var ledgerRepository: ILedgerRepository
    private lateinit var viewModel: TransactionHistoryViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        ledgerRepository = mock<ILedgerRepository>()
        viewModel = TransactionHistoryViewModel(ledgerRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadTransactionHistory_loads_transactions_successfully() = runTest {
        // Given
        val person = "Alice"
        val expectedTransactions = listOf(
            Transaction(person, 100.0, System.currentTimeMillis() / 1000, "Lunch"),
            Transaction(person, -50.0, System.currentTimeMillis() / 1000, "Movie")
        )
        whenever(ledgerRepository.getTransactionsForPerson(person)).thenReturn(expectedTransactions)

        // When
        viewModel.loadTransactionHistory(person)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(expectedTransactions, viewModel.transactions.value)
        assertEquals(null, viewModel.error.value)
    }

    @Test
    fun loadTransactionHistory_handles_error() = runTest {
        // Given
        val person = "Alice"
        val errorMessage = "Database error"
        whenever(ledgerRepository.getTransactionsForPerson(person)).thenThrow(
            RuntimeException(
                errorMessage
            )
        )

        // When
        viewModel.loadTransactionHistory(person)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(emptyList<Transaction>(), viewModel.transactions.value)
        assertEquals("Failed to load transaction history: $errorMessage", viewModel.error.value)
    }
}
