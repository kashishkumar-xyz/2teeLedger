package com.example.ledger.ffi;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public interface LedgerApi extends Library {
    LedgerApi INSTANCE = Native.load("ledger_lib", LedgerApi.class);

    int open_database(String path, String passphrase);

    int init_database();

    int add_transaction(String person, double amount, String note);

    Pointer get_all_balances(int[] len);

    Pointer get_transactions_for_person(String person, int[] len);

    void free_string(Pointer s);

    void free_balance_list(Pointer ptr, int len);

    void free_transaction_list(Pointer ptr, int len);

    Pointer get_last_error();

    @Structure.FieldOrder({"person", "amount", "timestamp", "note"})
    class Transaction extends Structure {
        public Pointer person;
        public double amount;
        public long timestamp;
        public Pointer note;

        public Transaction() {
        }

        public Transaction(Pointer p) {
            super(p);
        }
    }

    @Structure.FieldOrder({"person", "total"})
    class Balance extends Structure {
        public Pointer person;
        public double total;

        public Balance() {
        }

        public Balance(Pointer p) {
            super(p);
        }
    }
}
