use std::ffi::{CStr, CString};
use std::os::raw::c_char;
use crate::{backup, db, recovery};
use serde_json;

#[no_mangle]
pub extern "C" fn add_transaction(
    db_path: *const c_char,
    encryption_key: *const c_char,
    person: *const c_char,
    amount: i64,
    date: *const c_char,
    note: *const c_char,
) -> *const c_char {
    let db_path_str = unsafe { CStr::from_ptr(db_path).to_str().unwrap() };
    let encryption_key_str = unsafe { CStr::from_ptr(encryption_key).to_str().unwrap() };
    let mut key = db::EncryptionKey(encryption_key_str.to_string());

    let person_str = unsafe { CStr::from_ptr(person).to_str().unwrap() };
    let date_str = unsafe { CStr::from_ptr(date).to_str().unwrap() };
    let note_str = if note.is_null() {
        None
    } else {
        Some(unsafe { CStr::from_ptr(note).to_str().unwrap() })
    };

    match db::open_encrypted_db(db_path_str, &mut key) {
        Ok(conn) => {
            match db::add_transaction(&conn, person_str, amount, date_str, note_str) {
                Ok(_) => {
                    let success_msg = CString::new("Transaction added successfully").unwrap();
                    success_msg.into_raw()
                }
                Err(e) => {
                    let error_msg = CString::new(format!("Failed to add transaction: {}", e)).unwrap();
                    error_msg.into_raw()
                }
            }
        }
        Err(e) => {
            let error_msg = CString::new(format!("Failed to open database: {}", e)).unwrap();
            error_msg.into_raw()
        }
    }
}


#[no_mangle]
pub extern "C" fn init_db(db_path: *const c_char, encryption_key: *const c_char) -> *const c_char {
    let db_path_str = unsafe { CStr::from_ptr(db_path).to_str().unwrap() };
    let encryption_key_str = unsafe { CStr::from_ptr(encryption_key).to_str().unwrap() };
    let mut key = db::EncryptionKey(encryption_key_str.to_string());

    match db::open_encrypted_db(db_path_str, &mut key) {
        Ok(conn) => {
            match db::initialize_db(&conn) {
                Ok(_) => {
                    let success_msg = CString::new("Database initialized successfully").unwrap();
                    success_msg.into_raw()
                }
                Err(e) => {
                    let error_msg = CString::new(format!("Database initialization failed: {}", e)).unwrap();
                    error_msg.into_raw()
                }
            }
        }
        Err(e) => {
            let error_msg = CString::new(format!("Failed to open database: {}", e)).unwrap();
            error_msg.into_raw()
        }
    }
}

#[no_mangle]
pub extern "C" fn list_transactions(
    db_path: *const c_char,
    encryption_key: *const c_char,
    person: *const c_char,
    since_date: *const c_char,
    limit: i32,
) -> *const c_char {
    let db_path_str = unsafe { CStr::from_ptr(db_path).to_str().unwrap() };
    let encryption_key_str = unsafe { CStr::from_ptr(encryption_key).to_str().unwrap() };
    let mut key = db::EncryptionKey(encryption_key_str.to_string());

    let person_option = if person.is_null() { None } else { Some(unsafe { CStr::from_ptr(person).to_str().unwrap() }) };
    let since_date_option = if since_date.is_null() { None } else { Some(unsafe { CStr::from_ptr(since_date).to_str().unwrap() }) };
    let limit_option = if limit == 0 { None } else { Some(limit) };

    match db::open_encrypted_db(db_path_str, &mut key) {
        Ok(conn) => {
            match db::list_transactions(&conn, person_option, since_date_option, limit_option) {
                Ok(transactions) => {
                    let json_string = serde_json::to_string(&transactions).unwrap();
                    CString::new(json_string).unwrap().into_raw()
                }
                Err(e) => {
                    let error_msg = CString::new(format!("Failed to list transactions: {}", e)).unwrap();
                    error_msg.into_raw()
                }
            }
        }
        Err(e) => {
            let error_msg = CString::new(format!("Failed to open database: {}", e)).unwrap();
            error_msg.into_raw()
        }
    }
}

#[no_mangle]
pub extern "C" fn list_balances(
    db_path: *const c_char,
    encryption_key: *const c_char,
) -> *const c_char {
    let db_path_str = unsafe { CStr::from_ptr(db_path).to_str().unwrap() };
    let encryption_key_str = unsafe { CStr::from_ptr(encryption_key).to_str().unwrap() };
    let mut key = db::EncryptionKey(encryption_key_str.to_string());

    match db::open_encrypted_db(db_path_str, &mut key) {
        Ok(conn) => {
            match db::list_balances(&conn) {
                Ok(balances) => {
                    let json_string = serde_json::to_string(&balances).unwrap();
                    CString::new(json_string).unwrap().into_raw()
                }
                Err(e) => {
                    let error_msg = CString::new(format!("Failed to list balances: {}", e)).unwrap();
                    error_msg.into_raw()
                }
            }
        }
        Err(e) => {
            let error_msg = CString::new(format!("Failed to open database: {}", e)).unwrap();
            error_msg.into_raw()
        }
    }
}

#[no_mangle]
pub extern "C" fn get_balance(
    db_path: *const c_char,
    encryption_key: *const c_char,
    person: *const c_char,
) -> *const c_char {
    let db_path_str = unsafe { CStr::from_ptr(db_path).to_str().unwrap() };
    let encryption_key_str = unsafe { CStr::from_ptr(encryption_key).to_str().unwrap() };
    let mut key = db::EncryptionKey(encryption_key_str.to_string());
    let person_str = unsafe { CStr::from_ptr(person).to_str().unwrap() };

    match db::open_encrypted_db(db_path_str, &mut key) {
        Ok(conn) => {
            match db::get_balance(&conn, person_str) {
                Ok(balance) => {
                    let balance_string = balance.to_string();
                    CString::new(balance_string).unwrap().into_raw()
                }
                Err(e) => {
                    let error_msg = CString::new(format!("Failed to get balance: {}", e)).unwrap();
                    error_msg.into_raw()
                }
            }
        }
        Err(e) => {
            let error_msg = CString::new(format!("Failed to open database: {}", e)).unwrap();
            error_msg.into_raw()
        }
    }
}


#[no_mangle]
pub extern "C" fn restore_db(backup_path: *const c_char, db_path: *const c_char) -> *const c_char {
    let backup_path_str = unsafe { CStr::from_ptr(backup_path).to_str().unwrap() };
    let db_path_str = unsafe { CStr::from_ptr(db_path).to_str().unwrap() };

    match recovery::restore_db(backup_path_str, db_path_str) {
        Ok(_) => {
            let success_msg = CString::new("Restore successful").unwrap();
            success_msg.into_raw()
        }
        Err(e) => {
            let error_msg = CString::new(format!("Restore failed: {}", e)).unwrap();
            error_msg.into_raw()
        }
    }
}


#[no_mangle]
pub extern "C" fn backup_db(db_path: *const c_char, backup_path: *const c_char) -> *const c_char {
    let db_path_str = unsafe { CStr::from_ptr(db_path).to_str().unwrap() };
    let backup_path_str = unsafe { CStr::from_ptr(backup_path).to_str().unwrap() };

    match backup::backup_db(db_path_str, backup_path_str) {
        Ok(_) => {
            let success_msg = CString::new("Backup successful").unwrap();
            success_msg.into_raw()
        }
        Err(e) => {
            let error_msg = CString::new(format!("Backup failed: {}", e)).unwrap();
            error_msg.into_raw()
        }
    }
}
