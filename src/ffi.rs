use std::ffi::{CStr, CString};
use std::os::raw::c_char;
use crate::{backup, db, recovery};

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
