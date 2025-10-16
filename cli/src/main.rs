



use clap::{Parser, Subcommand};
use std::ffi::{CStr, CString};

#[derive(Parser)]
#[command(author, version, about, long_about = None)]
struct Cli {
    #[command(subcommand)]
    command: Commands,
}

#[derive(Subcommand)]
enum Commands {
    /// Adds a new transaction
    Add {
        #[arg(long)]
        db_path: String,
        #[arg(long)]
        encryption_key: String,
        #[arg(short, long)]
        person: String,
        #[arg(short, long)]
        amount: i32,
        #[arg(short, long)]
        date: String,
        #[arg(short, long)]
        note: Option<String>,
    },
    /// Lists transactions
    List {
        #[arg(short, long)]
        person: Option<String>,
        #[arg(long)]
        since_date: Option<String>,
        #[arg(short, long)]
        limit: Option<i32>,
    },
    /// Gets the balance for a person
    Balance {
        #[arg(short, long)]
        person: String,
    },
    /// Lists all balances
    Balances {},
    /// Initializes the database
    InitDb {
        #[arg(short, long)]
        db_path: String,
        #[arg(long)]
        encryption_key: String,
    },
    /// Opens the database
    OpenDb {
        #[arg(short, long)]
        db_path: String,
        #[arg(long)]
        encryption_key: String,
    },
    /// Backs up the database
    BackupDb {
        #[arg(short, long)]
        db_path: String,
        #[arg(long)]
        encryption_key: String,
        #[arg(long)]
        backup_path: String,
    },
    /// Restores the database
    RestoreDb {
        #[arg(short, long)]
        backup_path: String,
        #[arg(long)]
        encryption_key: String,
        #[arg(long)]
        db_path: String,
    },
}

fn main() {
    let cli = Cli::parse();

    match &cli.command {
        Commands::Add { db_path, encryption_key, person, amount, date, note } => {
            let db_path_c = CString::new(db_path.as_str()).unwrap();
            let key_c = CString::new(encryption_key.as_str()).unwrap();

            let person_c = CString::new(person.as_str()).unwrap();
            let date_c = CString::new(date.as_str()).unwrap();

            let note_c = note.as_ref().map(|s| CString::new(s.as_str()).unwrap());
            let note_ptr = note_c.as_ref().map_or(std::ptr::null(), |s| s.as_ptr());

            let result = ledger_lib::ffi::add_transaction(
                db_path_c.as_ptr(),
                key_c.as_ptr(),
                person_c.as_ptr(),
                *amount as i64,
                date_c.as_ptr(),
                note_ptr,
            );

            let result_str = unsafe { CStr::from_ptr(result).to_str().unwrap() };
            println!("{}", result_str);
            // Free the C string
            unsafe {
                let _ = CString::from_raw(result as *mut _);
            }
        }
        Commands::List { person, since_date, limit } => {
            println!("Listing transactions for {:?} since {:?} with limit {:?}", person, since_date, limit);
        }
        Commands::Balance { person } => {
            println!("Getting balance for {}", person);
        }
        Commands::Balances {} => {
            println!("Listing all balances");
        }
        Commands::InitDb { db_path, encryption_key } => {
                        let db_path_c = CString::new(db_path.as_str()).unwrap();
            let key_c = CString::new(encryption_key.as_str()).unwrap();

            let result = ledger_lib::ffi::init_db(db_path_c.as_ptr(), key_c.as_ptr());
            let result_str = unsafe { CStr::from_ptr(result).to_str().unwrap() };
            println!("{}", result_str);
            unsafe {
                let _ = CString::from_raw(result as *mut _);
            }
        }
        Commands::OpenDb { db_path, encryption_key } => {
            println!("Opening db at {} with key {}", db_path, encryption_key);
        }
        Commands::BackupDb { db_path, encryption_key, backup_path } => {
            println!("Backing up db at {} to {} with key {}", db_path, backup_path, encryption_key);
        }
        Commands::RestoreDb { backup_path, encryption_key, db_path } => {
            println!("Restoring db from {} to {} with key {}", backup_path, db_path, encryption_key);
        }
    }
}
