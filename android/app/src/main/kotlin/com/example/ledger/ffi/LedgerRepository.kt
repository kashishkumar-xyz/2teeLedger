package com.example.ledger.ffi

import android.content.Context
import android.util.Base64
import com.example.ledger.model.Balance
import com.example.ledger.model.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LedgerRepository(private val context: Context) : ILedgerRepository {

    companion object {
        init {
            System.loadLibrary("sqlcipher") // Load libsqlcipher.so first
            System.loadLibrary("ledger_lib")
        }
    }

    private val ledgerApi = LedgerApi.INSTANCE

    override suspend fun openDatabase(key: ByteArray) {
        withContext(Dispatchers.IO) {
            val dbPath = context.getDatabasePath("ledger.db").absolutePath
            val passphrase = Base64.encodeToString(key, Base64.NO_WRAP)

            // 1. Open the database
            val openResult = ledgerApi.open_database(dbPath, passphrase)
            if (openResult != 0) {
                val error =
                    ledgerApi.get_last_error()?.getString(0) ?: "Unknown error opening database"
                ledgerApi.free_string(ledgerApi.get_last_error())
                throw Exception("Failed to open database: $error")
            }

            // 2. Always attempt to initialize the database.
            //    The Rust side should handle this idempotently (e.g., CREATE TABLE IF NOT EXISTS).
            val initResult = ledgerApi.init_database()
            if (initResult != 0) {
                val error = ledgerApi.get_last_error()?.getString(0)
                    ?: "Unknown error initializing database"
                ledgerApi.free_string(ledgerApi.get_last_error())
                throw Exception("Failed to initialize database: $error")
            }
        }
    }

    override suspend fun addTransaction(person: String, amount: Double, note: String?) {
        withContext(Dispatchers.IO) {
            val result = ledgerApi.add_transaction(person, amount, note)
            if (result != 0) {
                val error = ledgerApi.get_last_error().getString(0)
                ledgerApi.free_string(ledgerApi.get_last_error())
                throw Exception("Failed to add transaction: $error")
            }
        }
    }

    override suspend fun getAllBalances(): List<Balance> {
        return withContext(Dispatchers.IO) {
            val len = intArrayOf(0)
            val ptr = ledgerApi.get_all_balances(len)
            val count = len[0]
            if (count == 0) {
                return@withContext emptyList()
            }
            val ffiBalances = mutableListOf<LedgerApi.Balance>()
            val structSize = LedgerApi.Balance().size()
            for (i in 0 until count) {
                val balance = LedgerApi.Balance(ptr.share((i * structSize).toLong()))
                balance.read()
                ffiBalances.add(balance)
            }
            val balances = ffiBalances.toList().map { Balance(it.person.getString(0), it.total) }
            ledgerApi.free_balance_list(ptr, count)
            balances
        }
    }

    override suspend fun getTransactionsForPerson(person: String): List<Transaction> {
        return withContext(Dispatchers.IO) {
            val len = intArrayOf(0)
            val ptr = ledgerApi.get_transactions_for_person(person, len)
            val count = len[0]
            if (count == 0) {
                return@withContext emptyList()
            }
            val ffiTransactions = mutableListOf<LedgerApi.Transaction>()
            val structSize = LedgerApi.Transaction().size()
            for (i in 0 until count) {
                val transaction = LedgerApi.Transaction(ptr.share((i * structSize).toLong()))
                transaction.read()
                ffiTransactions.add(transaction)
            }
            val transactions = ffiTransactions.toList().map {
                Transaction(
                    it.person.getString(0),
                    it.amount,
                    it.timestamp,
                    it.note?.getString(0)
                )
            }
            ledgerApi.free_transaction_list(ptr, count)
            transactions
        }
    }
}
