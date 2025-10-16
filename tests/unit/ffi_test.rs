use ledger::ffi::{init_db, add_transaction};
use std::ffi::CString;
use std::ptr;
use tempfile::NamedTempFile;

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
