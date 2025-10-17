package com.example.teeledger.jni

object LedgerCore {

    init {
        System.loadLibrary("ledger_lib")
    }

    external fun init_db(db_path: String, encryption_key: String): String

    external fun open_db(db_path: String, encryption_key: String): String

    external fun add_transaction(db_path: String, encryption_key: String, person: String, amount: Long, date: String, note: String?): String

    external fun list_transactions(db_path: String, encryption_key: String, person: String?, since_date: String?, limit: Int): String

    external fun list_balances(db_path: String, encryption_key: String): String

    external fun get_balance(db_path: String, encryption_key: String, person: String): String

    external fun backup_db(db_path: String, backup_path: String): String

    external fun restore_db(backup_path: String, db_path: String): String
}
