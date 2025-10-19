use std::os::raw::c_char;
use crate::db;
use std::ffi::{CStr, CString};
use std::sync::Mutex;
use lazy_static::lazy_static;
use rusqlite::Connection;
use chrono::Utc;
use std::cell::RefCell;

lazy_static! {
    static ref DB_CONNECTION: Mutex<Option<Connection>> = Mutex::new(None);
}

/// A transaction record.
#[repr(C)]
pub struct Transaction {
    /// The person associated with the transaction.
    pub person: *const c_char,
    /// The amount of the transaction.
    pub amount: f64,
    /// The timestamp of the transaction.
    pub timestamp: i64,
    /// An optional note for the transaction.
    pub note: *const c_char, // Can be null
}

/// A balance record.
#[repr(C)]
pub struct Balance {
    /// The person associated with the balance.
    pub person: *const c_char,
    /// The total balance for the person.
    pub total: f64,
}

/// Opens the database.
///
/// # Arguments
///
/// * `path` - The path to the database file.
/// * `passphrase` - The passphrase for the database.
///
/// # Returns
///
/// Returns `0` if the database was opened successfully, or `-1` if the database failed to open.
#[no_mangle]
pub unsafe extern "C" fn open_database(path: *const c_char, passphrase: *const c_char) -> i32 {
    let path_str = CStr::from_ptr(path).to_str().unwrap();
    let passphrase_str = CStr::from_ptr(passphrase).to_str().unwrap();
    let mut key = db::EncryptionKey(passphrase_str.to_string());

    match db::open_encrypted_db(path_str, &mut key, false) {
        Ok(conn) => {
            let mut db_conn = DB_CONNECTION.lock().unwrap();
            *db_conn = Some(conn);
            0
        }
        Err(e) => {
            update_last_error(&e.to_string());
            -1
        }
    }
}

/// Initializes the database.
///
/// # Returns
///
/// Returns `0` if the database was initialized successfully, or `-1` if the database failed to initialize.
#[no_mangle]
pub unsafe extern "C" fn init_database() -> i32 {
    let db_conn = DB_CONNECTION.lock().unwrap();
    if let Some(conn) = &*db_conn {
        match db::initialize_db(conn) {
            Ok(_) => 0,
            Err(e) => {
                update_last_error(&e.to_string());
                -1
            }
        }
    } else {
        update_last_error("Database not open");
        -1
    }
}

/// Adds a new transaction to the database.
///
/// # Arguments
///
/// * `person` - The person associated with the transaction.
/// * `amount` - The amount of the transaction.
/// * `note` - An optional note for the transaction.
///
/// # Returns
///
/// Returns `0` if the transaction was added successfully, or `-1` if the transaction failed to add.
#[no_mangle]
pub unsafe extern "C" fn add_transaction(person: *const c_char, amount: f64, note: *const c_char) -> i32 {
    let person_str = CStr::from_ptr(person).to_str().unwrap();
    if person_str.is_empty() {
        update_last_error("Person cannot be empty");
        return -1; // Invalid input
    }
    if amount == 0.0 {
        update_last_error("Amount cannot be zero");
        return -1; // Invalid input
    }

    let note_str = if note.is_null() {
        None
    } else {
        Some(CStr::from_ptr(note).to_str().unwrap())
    };

    let db_conn = DB_CONNECTION.lock().unwrap();
    if let Some(conn) = &*db_conn {
        let date = Utc::now().to_rfc3339();
        match db::add_transaction(conn, person_str, amount as i64, &date, note_str) {
            Ok(_) => 0,
            Err(e) => {
                update_last_error(&e.to_string());
                -1
            }
        }
    } else {
        update_last_error("Database not open");
        -1
    }
}

/// Gets all balances from the database.
///
/// # Arguments
///
/// * `len` - A pointer to a u32 that will be filled with the number of balances.
///
/// # Returns
///
/// Returns a pointer to an array of balances, or `null` if the query failed.
#[no_mangle]
pub unsafe extern "C" fn get_all_balances(len: *mut u32) -> *const Balance {
    let db_conn = DB_CONNECTION.lock().unwrap();
    if let Some(conn) = &*db_conn {
        match db::list_balances(conn) {
            Ok(balances) => {
                let mut ffi_balances = Vec::with_capacity(balances.len());
                for balance in balances {
                    let person = CString::new(balance.person).unwrap().into_raw();
                    ffi_balances.push(Balance { person, total: balance.balance as f64 });
                }
                *len = ffi_balances.len() as u32;
                let ptr = ffi_balances.as_ptr();
                std::mem::forget(ffi_balances);
                ptr
            }
            Err(e) => {
                update_last_error(&e.to_string());
                *len = 0;
                std::ptr::null()
            }
        }
    } else {
        update_last_error("Database not open");
        *len = 0;
        std::ptr::null()
    }
}

/// Gets all transactions for a specific person.
///
/// # Arguments
///
/// * `person` - The person to get the transactions for.
/// * `len` - A pointer to a u32 that will be filled with the number of transactions.
///
/// # Returns
///
/// Returns a pointer to an array of transactions, or `null` if the query failed.
#[no_mangle]
pub unsafe extern "C" fn get_transactions_for_person(person: *const c_char, len: *mut u32) -> *const Transaction {
    let person_str = CStr::from_ptr(person).to_str().unwrap();
    let db_conn = DB_CONNECTION.lock().unwrap();
    if let Some(conn) = &*db_conn {
        match db::list_transactions(conn, Some(person_str), None, None) {
            Ok(transactions) => {
                let mut ffi_transactions = Vec::with_capacity(transactions.len());
                for transaction in transactions {
                    let person = CString::new(transaction.person).unwrap().into_raw();
                    let note = transaction.note.map(|s| CString::new(s).unwrap().into_raw()).unwrap_or(core::ptr::null_mut());
                    let timestamp = chrono::DateTime::parse_from_rfc3339(&transaction.date).unwrap().timestamp();
                    ffi_transactions.push(Transaction { person, amount: transaction.amount as f64, timestamp, note });
                }
                *len = ffi_transactions.len() as u32;
                let ptr = ffi_transactions.as_ptr();
                std::mem::forget(ffi_transactions);
                ptr
            }
            Err(e) => {
                update_last_error(&e.to_string());
                *len = 0;
                std::ptr::null()
            }
        }
    } else {
        update_last_error("Database not open");
        *len = 0;
        std::ptr::null()
    }
}

/// Frees a C string that was allocated by the Rust side.
///
/// # Arguments
///
/// * `s` - The C string to free.
#[no_mangle]
pub unsafe extern "C" fn free_string(s: *mut c_char) {
    if s.is_null() { return; }
    let _ = CString::from_raw(s);
}

/// Frees a list of balances that was allocated by the Rust side.
///
/// # Arguments
///
/// * `ptr` - A pointer to the list of balances to free.
/// * `len` - The number of balances in the list.
#[no_mangle]
pub unsafe extern "C" fn free_balance_list(ptr: *mut Balance, len: u32) {
    if ptr.is_null() { return; }
    let balances = Vec::from_raw_parts(ptr, len as usize, len as usize);
    for balance in balances {
        let _ = CString::from_raw(balance.person as *mut c_char);
    }
}

/// Frees a list of transactions that was allocated by the Rust side.
///
/// # Arguments
///
/// * `ptr` - A pointer to the list of transactions to free.
/// * `len` - The number of transactions in the list.
#[no_mangle]
pub unsafe extern "C" fn free_transaction_list(ptr: *mut Transaction, len: u32) {
    if ptr.is_null() { return; }
    let transactions = Vec::from_raw_parts(ptr, len as usize, len as usize);
    for transaction in transactions {
        let _ = CString::from_raw(transaction.person as *mut c_char);
        if !transaction.note.is_null() {
            let _ = CString::from_raw(transaction.note as *mut c_char);
        }
    }
}

thread_local! {
    static LAST_ERROR: RefCell<Option<String>> = RefCell::new(None);
}

/// Gets the last error message that was set.
///
/// # Returns
///
/// Returns a C string containing the last error message, or `null` if no error was set.
/// The caller is responsible for freeing this string using `free_string`.
#[no_mangle]
pub extern "C" fn get_last_error() -> *const c_char {
    let mut error_message: *const c_char = std::ptr::null();
    LAST_ERROR.with(|error| {
        if let Some(e) = &*error.borrow() {
            error_message = CString::new(e.clone()).unwrap().into_raw();
        }
    });
    error_message
}

fn update_last_error(err: &str) {
    LAST_ERROR.with(|error| {
        *error.borrow_mut() = Some(err.to_string());
    });
}