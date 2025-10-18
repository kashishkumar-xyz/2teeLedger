package com.example.ledger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import com.example.ledger.security.KeyManager
import com.example.ledger.ffi.LedgerRepository
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val keyManager = KeyManager(applicationContext)
        val key: ByteArray = keyManager.getOrCreateDatabaseKey()

        val ledgerRepository = LedgerRepository(applicationContext)
        lifecycleScope.launch {
            ledgerRepository.openDatabase(key)
        }

        setContent {
            Greeting("Android")
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}
