//! # Ledger CLI
//!
//! A command-line interface for managing a ledger database.
//!
//! This CLI provides a set of commands for interacting with the ledger,
//! including adding transactions, listing transactions, and managing the database.

use clap::{Parser, Subcommand};
use std::ffi::{CStr, CString};

/// The main command-line interface for the ledger.
#[derive(Parser)]
#[command(author, version, about, long_about = None)]
struct Cli {
    /// The subcommand to execute.
    #[command(subcommand)]
    command: Commands,
}

/// The available subcommands for the CLI.
#[derive(Subcommand)]
enum Commands {
    /// Adds a new transaction to the ledger.
    Add {
        /// The path to the database file.
        #[arg(long)]
        db_path: String,
        /// The encryption key for the database.
        #[arg(long)]
        encryption_key: String,
        /// The person associated with the transaction.
        #[arg(short, long)]
        person: String,
        /// The amount of the transaction.
        #[arg(short, long, allow_hyphen_values = true)]
        amount: i32,
        /// The date of the transaction.
        #[arg(short, long, allow_hyphen_values = true)]
        date: String,
        /// An optional note for the transaction.
        #[arg(short, long)]
        note: Option<String>,
    },
    /// Lists transactions from the ledger.
    List {
        /// The path to the database file.
        #[arg(long)]
        db_path: String,
        /// The encryption key for the database.
        #[arg(long)]
        encryption_key: String,
        /// The person to filter transactions by.
        #[arg(short, long)]
        person: Option<String>,
        /// The date to start listing transactions from.
        #[arg(long)]
        since_date: Option<String>,
        /// The maximum number of transactions to list.
        #[arg(short, long)]
        limit: Option<i32>,
    },
    /// Gets the balance for a specific person.
    Balance {
        /// The path to the database file.
        #[arg(long)]
        db_path: String,
        /// The encryption key for the database.
        #[arg(long)]
        encryption_key: String,
        /// The person to get the balance for.
        #[arg(short, long)]
        person: String,
    },
    /// Lists all balances in the ledger.
    Balances {
        /// The path to the database file.
        #[arg(long)]
        db_path: String,
        /// The encryption key for the database.
        #[arg(long)]
        encryption_key: String,
    },
    /// Initializes a new ledger database.
    InitDb {
        /// The path to the database file.
        #[arg(short, long)]
        db_path: String,
        /// The encryption key for the database.
        #[arg(long)]
        encryption_key: String,
    },
    /// Opens an existing ledger database.
    OpenDb {
        /// The path to the database file.
        #[arg(short, long)]
        db_path: String,
        /// The encryption key for the database.
        #[arg(long)]
        encryption_key: String,
    },
    /// Backs up the ledger database.
    BackupDb {
        /// The path to the database file.
        #[arg(short, long)]
        db_path: String,
        /// The encryption key for the database.
        #[arg(long)]
        encryption_key: String,
        /// The path to the backup file.
        #[arg(long)]
        backup_path: String,
    },
    /// Restores the ledger database from a backup.
    RestoreDb {
        /// The path to the backup file.
        #[arg(short, long)]
        backup_path: String,
        /// The encryption key for the database.
        #[arg(long)]
        encryption_key: String,
        /// The path to the database file.
        #[arg(long)]
        db_path: String,
    },
}

/// The main entry point for the CLI.
///
/// This function parses the command-line arguments and executes the appropriate subcommand.
///
/// # Returns
///
/// Returns `Ok(())` if the command was successful, or an error if the command failed.
fn main() -> Result<(), Box<dyn std::error::Error>> {
    let cli = Cli::parse();

    match &cli.command {
        Commands::Add {
            db_path,
            encryption_key,
            person,
            amount,
            date,
            note,
        } => {
            let db_path_c = CString::new(db_path.as_str()).unwrap();

            let key_c = CString::new(encryption_key.as_str()).unwrap();

            let person_c = CString::new(person.as_str()).unwrap();

            let date_c = CString::new(date.as_str()).unwrap();

            let note_c = note.as_ref().map(|s| CString::new(s.as_str()).unwrap());

            let note_ptr = note_c.as_ref().map_or(std::ptr::null(), |s| s.as_ptr());

            let result = unsafe {
                ledger_lib::ffi::add_transaction(
                    db_path_c.as_ptr(),
                    key_c.as_ptr(),
                    person_c.as_ptr(),
                    *amount as i64,
                    date_c.as_ptr(),
                    note_ptr,
                )
            };

            let result_str = unsafe { CStr::from_ptr(result).to_str().unwrap() };

            println!("{}", result_str);

            // Free the C string

            unsafe {
                let _ = CString::from_raw(result as *mut _);
            }

            if result_str.contains("Failed to add transaction") {
                Err(result_str.into())
            } else {
                Ok(())
            }
        }

        Commands::List {
            db_path,
            encryption_key,
            person,
            since_date,
            limit,
        } => {
            let db_path_c = CString::new(db_path.as_str()).unwrap();

            let key_c = CString::new(encryption_key.as_str()).unwrap();

            let person_ptr = person.as_ref().map_or(std::ptr::null(), |s| {
                CString::new(s.as_str()).unwrap().into_raw()
            });

            let since_date_ptr = since_date.as_ref().map_or(std::ptr::null(), |s| {
                CString::new(s.as_str()).unwrap().into_raw()
            });

            let limit_val = limit.unwrap_or(0);

            let result = unsafe {
                ledger_lib::ffi::list_transactions(
                    db_path_c.as_ptr(),
                    key_c.as_ptr(),
                    person_ptr,
                    since_date_ptr,
                    limit_val,
                )
            };

            let result_str = unsafe { CStr::from_ptr(result).to_str().unwrap() };

            println!("{}", result_str);

            // Free the C strings

            unsafe {
                if !person_ptr.is_null() {
                    let _ = CString::from_raw(person_ptr as *mut _);
                }

                if !since_date_ptr.is_null() {
                    let _ = CString::from_raw(since_date_ptr as *mut _);
                }

                let _ = CString::from_raw(result as *mut _);
            }

            if result_str.contains("Failed to list transactions") {
                Err(result_str.into())
            } else {
                Ok(())
            }
        }

        Commands::Balance {
            db_path,
            encryption_key,
            person,
        } => {
            let db_path_c = CString::new(db_path.as_str()).unwrap();

            let key_c = CString::new(encryption_key.as_str()).unwrap();

            let person_c = CString::new(person.as_str()).unwrap();

            let result = unsafe {
                ledger_lib::ffi::get_balance(db_path_c.as_ptr(), key_c.as_ptr(), person_c.as_ptr())
            };

            let result_str = unsafe { CStr::from_ptr(result).to_str().unwrap() };

            println!("{}", result_str);

            unsafe {
                let _ = CString::from_raw(result as *mut _);
            }

            if result_str.contains("Failed to get balance") {
                Err(result_str.into())
            } else {
                Ok(())
            }
        }

        Commands::Balances {
            db_path,
            encryption_key,
        } => {
            let db_path_c = CString::new(db_path.as_str()).unwrap();

            let key_c = CString::new(encryption_key.as_str()).unwrap();

            let result =
                unsafe { ledger_lib::ffi::list_balances(db_path_c.as_ptr(), key_c.as_ptr()) };

            let result_str = unsafe { CStr::from_ptr(result).to_str().unwrap() };

            println!("{}", result_str);

            unsafe {
                let _ = CString::from_raw(result as *mut _);
            }

            if result_str.contains("Failed to list balances") {
                Err(result_str.into())
            } else {
                Ok(())
            }
        }

        Commands::InitDb {
            db_path,
            encryption_key,
        } => {
            let db_path_c = CString::new(db_path.as_str()).unwrap();

            let key_c = CString::new(encryption_key.as_str()).unwrap();

            let result = unsafe { ledger_lib::ffi::init_db(db_path_c.as_ptr(), key_c.as_ptr()) };

            let result_str = unsafe { CStr::from_ptr(result).to_str().unwrap() };

            println!("{}", result_str);

            unsafe {
                let _ = CString::from_raw(result as *mut _);
            }

            if result_str.contains("Database initialization failed") {
                Err(result_str.into())
            } else {
                Ok(())
            }
        }

        Commands::OpenDb {
            db_path,
            encryption_key,
        } => {
            let db_path_c = CString::new(db_path.as_str()).unwrap();

            let key_c = CString::new(encryption_key.as_str()).unwrap();

            let result = unsafe { ledger_lib::ffi::open_db(db_path_c.as_ptr(), key_c.as_ptr()) };

            let result_str = unsafe {
                CStr::from_ptr(result)
                    .to_str()
                    .unwrap_or_else(|_| "Failed to open database: Invalid UTF-8 sequence")
            };

            let is_error = result_str.contains("Failed to open database");

            if is_error {
                eprintln!("{}", result_str);
            } else {
                println!("{}", result_str);
            }

            unsafe {
                let _ = CString::from_raw(result as *mut _);
            }

            if is_error {
                Err(result_str.into())
            } else {
                Ok(())
            }
        }

        Commands::BackupDb {
            db_path,
            encryption_key: _,
            backup_path,
        } => {
            let db_path_c = CString::new(db_path.as_str()).unwrap();

            let backup_path_c = CString::new(backup_path.as_str()).unwrap();

            let result =
                unsafe { ledger_lib::ffi::backup_db(db_path_c.as_ptr(), backup_path_c.as_ptr()) };

            let result_str = unsafe { CStr::from_ptr(result).to_str().unwrap() };

            println!("{}", result_str);

            unsafe {
                let _ = CString::from_raw(result as *mut _);
            }

            if result_str.contains("Backup failed") {
                Err(result_str.into())
            } else {
                Ok(())
            }
        }

        Commands::RestoreDb {
            backup_path,
            encryption_key: _,
            db_path,
        } => {
            let backup_path_c = CString::new(backup_path.as_str()).unwrap();

            let db_path_c = CString::new(db_path.as_str()).unwrap();

            let result =
                unsafe { ledger_lib::ffi::restore_db(backup_path_c.as_ptr(), db_path_c.as_ptr()) };

            let result_str = unsafe { CStr::from_ptr(result).to_str().unwrap() };

            println!("{}", result_str);

            unsafe {
                let _ = CString::from_raw(result as *mut _);
            }

            if result_str.contains("Restore failed") {
                Err(result_str.into())
            } else {
                Ok(())
            }
        }
    }
}
