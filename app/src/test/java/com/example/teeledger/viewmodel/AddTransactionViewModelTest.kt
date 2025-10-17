package com.example.teeledger.viewmodel

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class AddTransactionViewModelTest {

    private lateinit var viewModel: AddTransactionViewModel

    @Before
    fun setup() {
        viewModel = AddTransactionViewModel()
    }

    @Test
    fun `addTransaction returns false for empty person`() {
        val result = viewModel.addTransaction("", 1000, "Test note")
        assertFalse(result)
    }

    @Test
    fun `addTransaction returns false for zero amount`() {
        val result = viewModel.addTransaction("John Doe", 0, "Test note")
        assertFalse(result)
    }
}
