use clap::{Parser, Subcommand};

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
        Commands::Add { person, amount, date, note } => {
            println!("Adding transaction for {}: {} on {} with note: {:?}", person, amount, date, note);
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
            println!("Initializing db at {} with key {}", db_path, encryption_key);
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
