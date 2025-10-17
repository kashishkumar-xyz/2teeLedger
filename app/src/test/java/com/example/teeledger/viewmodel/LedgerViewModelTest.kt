package com.example.teeledger.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Test

class LedgerViewModelTest {

    @Test
    fun testGetBalances() {
        // This is a placeholder test that will fail until the ViewModel is implemented
        val viewModel = LedgerViewModel()
        val balances = viewModel.getBalances_for_test()
        assertEquals(0, balances.size)
    }
}
