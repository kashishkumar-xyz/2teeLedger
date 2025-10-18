package com.example.ledger.security

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class KeyManagerTest {

    private lateinit var keyManager: KeyManager

    @Before
    fun setup() {
        keyManager = KeyManager()
    }

    @Test
    fun getOrCreateKey_returnsSameKey() {
        val key1 = keyManager.getOrCreateKey()
        val key2 = keyManager.getOrCreateKey()
        assertEquals(key1, key2)
    }
}
