package com.example.teeledger.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Test

class TransactionHistoryViewModelTest {

    @Test
    fun testGetTransactionHistory() {
        // This is a placeholder test that will fail until the ViewModel is implemented
        val viewModel = TransactionHistoryViewModel()
        val transactions = viewModel.getTransactionHistory_for_test("John Doe")
        assertEquals(0, transactions.size)
    }
}
