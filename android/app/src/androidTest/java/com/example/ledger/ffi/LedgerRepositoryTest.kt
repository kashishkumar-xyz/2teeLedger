package com.example.ledger.ffi

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.ledger.security.KeyManager
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LedgerRepositoryTest {

    private lateinit var ledgerRepository: LedgerRepository

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        ledgerRepository = LedgerRepository(context)
        val keyManager = KeyManager()
        val key = keyManager.getOrCreateKey()
        runBlocking { ledgerRepository.openDatabase(key) }
    }

    @Test
    fun testAddAndGet() = runBlocking {
        ledgerRepository.addTransaction("test_person", 100.0, "test_note")

        val balances = ledgerRepository.getAllBalances()
        assertEquals(1, balances.size)
        assertEquals("test_person", balances[0].person)
        assertEquals(100.0, balances[0].total, 0.0)

        val transactions = ledgerRepository.getTransactionsForPerson("test_person")
        assertEquals(1, transactions.size)
        assertEquals("test_person", transactions[0].person)
        assertEquals(100.0, transactions[0].amount, 0.0)
        assertEquals("test_note", transactions[0].note)
    }
}
