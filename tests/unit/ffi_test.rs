use ledger_lib::ffi::{init_db, add_transaction, list_transactions, list_balances, get_balance};
use std::ffi::CString;
use std::ptr;
use tempfile::NamedTempFile;
use serde_json::Value;

#[test]
fn test_add_transaction_ffi() {
    let db_file = NamedTempFile::new().unwrap();
    let db_path = CString::new(db_file.path().to_str().unwrap()).unwrap();
    let key = CString::new("test_key").unwrap();

    let result = init_db(db_path.as_ptr(), key.as_ptr());
    let result_str = unsafe { CString::from_raw(result as *mut _) };
    assert_eq!(result_str.to_str().unwrap(), "Database initialized successfully");

    let person = CString::new("John Doe").unwrap();
    let date = CString::new("2025-10-26").unwrap();
    let note = CString::new("Test").unwrap();

    let result = add_transaction(
        db_path.as_ptr(),
        key.as_ptr(),
        person.as_ptr(),
        1000,
        date.as_ptr(),
        note.as_ptr(),
    );
    let result_str = unsafe { CString::from_raw(result as *mut _) };
    assert_eq!(result_str.to_str().unwrap(), "Transaction added successfully");
}

#[test]
fn test_list_transactions_ffi() {
    let db_file = NamedTempFile::new().unwrap();
    let db_path = CString::new(db_file.path().to_str().unwrap()).unwrap();
    let key = CString::new("test_key").unwrap();

    // Initialize DB
    let init_result = init_db(db_path.as_ptr(), key.as_ptr());
    let _ = unsafe { CString::from_raw(init_result as *mut _) }; // Free CString

    // Add transactions
    let person1 = CString::new("Alice").unwrap();
    let date1 = CString::new("2025-01-01").unwrap();
    let add_result1 = add_transaction(db_path.as_ptr(), key.as_ptr(), person1.as_ptr(), 100, date1.as_ptr(), ptr::null());
    let _ = unsafe { CString::from_raw(add_result1 as *mut _) };

    let person2 = CString::new("Bob").unwrap();
    let date2 = CString::new("2025-01-02").unwrap();
    let add_result2 = add_transaction(db_path.as_ptr(), key.as_ptr(), person2.as_ptr(), 200, date2.as_ptr(), ptr::null());
    let _ = unsafe { CString::from_raw(add_result2 as *mut _) };

    // List all transactions
    let list_result = list_transactions(db_path.as_ptr(), key.as_ptr(), ptr::null(), ptr::null(), 0);
    let list_result_str = unsafe { CString::from_raw(list_result as *mut _) };
    let transactions: Value = serde_json::from_str(list_result_str.to_str().unwrap()).unwrap();

    assert!(transactions.is_array());
    assert_eq!(transactions.as_array().unwrap().len(), 2);
    assert_eq!(transactions.as_array().unwrap()[0]["person"], "Bob"); // Ordered by date DESC
    assert_eq!(transactions.as_array().unwrap()[1]["person"], "Alice");

    // List transactions for Alice
    let person_filter = CString::new("Alice").unwrap();
    let list_result_alice = list_transactions(db_path.as_ptr(), key.as_ptr(), person_filter.as_ptr(), ptr::null(), 0);
    let list_result_alice_str = unsafe { CString::from_raw(list_result_alice as *mut _) };
    let alice_transactions: Value = serde_json::from_str(list_result_alice_str.to_str().unwrap()).unwrap();

    assert!(alice_transactions.is_array());
    assert_eq!(alice_transactions.as_array().unwrap().len(), 1);
    assert_eq!(alice_transactions.as_array().unwrap()[0]["person"], "Alice");
}

#[test]
fn test_list_balances_ffi() {
    let db_file = NamedTempFile::new().unwrap();
    let db_path = CString::new(db_file.path().to_str().unwrap()).unwrap();
    let key = CString::new("test_key").unwrap();

    // Initialize DB
    let init_result = init_db(db_path.as_ptr(), key.as_ptr());
    let _ = unsafe { CString::from_raw(init_result as *mut _) };

    // Add transactions
    let person1 = CString::new("Alice").unwrap();
    let date1 = CString::new("2025-01-01").unwrap();
    let add_result1 = add_transaction(db_path.as_ptr(), key.as_ptr(), person1.as_ptr(), 100, date1.as_ptr(), ptr::null());
    let _ = unsafe { CString::from_raw(add_result1 as *mut _) };

    let person2 = CString::new("Bob").unwrap();
    let date2 = CString::new("2025-01-02").unwrap();
    let add_result2 = add_transaction(db_path.as_ptr(), key.as_ptr(), person2.as_ptr(), 200, date2.as_ptr(), ptr::null());
    let _ = unsafe { CString::from_raw(add_result2 as *mut _) };

    let person3 = CString::new("Alice").unwrap();
    let date3 = CString::new("2025-01-03").unwrap();
    let add_result3 = add_transaction(db_path.as_ptr(), key.as_ptr(), person3.as_ptr(), -50, date3.as_ptr(), ptr::null());
    let _ = unsafe { CString::from_raw(add_result3 as *mut _) };

    // List balances
    let list_balances_result = list_balances(db_path.as_ptr(), key.as_ptr());
    let list_balances_result_str = unsafe { CString::from_raw(list_balances_result as *mut _) };
    let balances: Value = serde_json::from_str(list_balances_result_str.to_str().unwrap()).unwrap();

    assert!(balances.is_array());
    assert_eq!(balances.as_array().unwrap().len(), 2);

    let alice_balance = balances.as_array().unwrap().iter().find(|b| b["person"] == "Alice").unwrap();
    assert_eq!(alice_balance["balance"], 50);

    let bob_balance = balances.as_array().unwrap().iter().find(|b| b["person"] == "Bob").unwrap();
    assert_eq!(bob_balance["balance"], 200);
}

#[test]
fn test_get_balance_ffi() {
    let db_file = NamedTempFile::new().unwrap();
    let db_path = CString::new(db_file.path().to_str().unwrap()).unwrap();
    let key = CString::new("test_key").unwrap();

    // Initialize DB
    let init_result = init_db(db_path.as_ptr(), key.as_ptr());
    let _ = unsafe { CString::from_raw(init_result as *mut _) };

    // Add transactions
    let person1 = CString::new("Alice").unwrap();
    let date1 = CString::new("2025-01-01").unwrap();
    let add_result1 = add_transaction(db_path.as_ptr(), key.as_ptr(), person1.as_ptr(), 100, date1.as_ptr(), ptr::null());
    let _ = unsafe { CString::from_raw(add_result1 as *mut _) };

    let person2 = CString::new("Bob").unwrap();
    let date2 = CString::new("2025-01-02").unwrap();
    let add_result2 = add_transaction(db_path.as_ptr(), key.as_ptr(), person2.as_ptr(), 200, date2.as_ptr(), ptr::null());
    let _ = unsafe { CString::from_raw(add_result2 as *mut _) };

    let person3 = CString::new("Alice").unwrap();
    let date3 = CString::new("2025-01-03").unwrap();
    let add_result3 = add_transaction(db_path.as_ptr(), key.as_ptr(), person3.as_ptr(), -50, date3.as_ptr(), ptr::null());
    let _ = unsafe { CString::from_raw(add_result3 as *mut _) };

    // Get balance for Alice
    let person_alice = CString::new("Alice").unwrap();
    let alice_balance_result = get_balance(db_path.as_ptr(), key.as_ptr(), person_alice.as_ptr());
    let alice_balance_str = unsafe { CString::from_raw(alice_balance_result as *mut _) };
    assert_eq!(alice_balance_str.to_str().unwrap(), "50");

    // Get balance for Bob
    let person_bob = CString::new("Bob").unwrap();
    let bob_balance_result = get_balance(db_path.as_ptr(), key.as_ptr(), person_bob.as_ptr());
    let bob_balance_str = unsafe { CString::from_raw(bob_balance_result as *mut _) };
    assert_eq!(bob_balance_str.to_str().unwrap(), "200");

    // Get balance for Charlie (non-existent)
    let person_charlie = CString::new("Charlie").unwrap();
    let charlie_balance_result = get_balance(db_path.as_ptr(), key.as_ptr(), person_charlie.as_ptr());
    let charlie_balance_str = unsafe { CString::from_raw(charlie_balance_result as *mut _) };
    assert_eq!(charlie_balance_str.to_str().unwrap(), "0");
}