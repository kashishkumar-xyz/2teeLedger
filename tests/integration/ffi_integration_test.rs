use ledger_lib::ffi::{add_transaction, get_all_balances, get_last_error, get_transactions_for_person, open_database, init_database, free_balance_list, free_transaction_list, free_string};
use std::ffi::{CStr, CString};
use std::os::raw::c_char;
use tempfile::NamedTempFile;

#[test]
fn test_ffi() {
    let db_file = NamedTempFile::new().unwrap();
    let db_path = CString::new(db_file.path().to_str().unwrap()).unwrap();
    let passphrase = CString::new("test").unwrap();

    unsafe {
        // Test open_database
        let result = open_database(db_path.as_ptr(), passphrase.as_ptr());
        assert_eq!(result, 0);

        // Test init_database
        let result = init_database();
        assert_eq!(result, 0);

        // Test add_transaction
        let person = CString::new("test_person").unwrap();
        let result = add_transaction(person.as_ptr(), 100.0, std::ptr::null());
        assert_eq!(result, 0);

        // Test get_all_balances
        let mut len = 0;
        let balances_ptr = get_all_balances(&mut len);
        assert_eq!(len, 1);
        let balances = std::slice::from_raw_parts(balances_ptr, len as usize);
        assert_eq!(CStr::from_ptr(balances[0].person).to_str().unwrap(), "test_person");
        assert_eq!(balances[0].total, 100.0);
        free_balance_list(balances_ptr as *mut _, len);

        // Test get_transactions_for_person
        let mut len = 0;
        let transactions_ptr = get_transactions_for_person(person.as_ptr(), &mut len);
        assert_eq!(len, 1);
        let transactions = std::slice::from_raw_parts(transactions_ptr, len as usize);
        assert_eq!(CStr::from_ptr(transactions[0].person).to_str().unwrap(), "test_person");
        assert_eq!(transactions[0].amount, 100.0);
        free_transaction_list(transactions_ptr as *mut _, len);

        // Test error handling
        let invalid_person = CString::new("").unwrap();
        let result = add_transaction(invalid_person.as_ptr(), 100.0, std::ptr::null());
        assert_eq!(result, -1);
        let error_ptr = get_last_error();
        let error_str = CStr::from_ptr(error_ptr).to_str().unwrap();
        assert_eq!(error_str, "Person cannot be empty");
        free_string(error_ptr as *mut _);
    }
}
