# Ledger

This project is a simple ledger application that allows you to track your transactions. It is composed of a Rust library that manages the ledger, a Rust CLI that uses the library, and an Android application that also uses the library.

## Architecture

The core of the project is the `ledger` library, which is a Rust library that provides a set of APIs for managing a ledger. The library is responsible for all database operations, including adding transactions, listing transactions, and calculating balances. The database is encrypted using SQLCipher.

The `cli` is a Rust command-line application that uses the `ledger` library to provide a command-line interface for managing the ledger.

The `android` application is an Android application that uses the `ledger` library to provide a graphical user interface for managing the ledger. The `ledger` library is integrated into the Android application using JNA.

## Building and Running

### CLI

To build the CLI, you will need to have Rust installed. You can then build the CLI by running the following command in the `cli` directory:

```
cargo build
```

To run the CLI, you can use the following command in the `cli` directory:

```
cargo run -- <command>
```

For example, to add a new transaction, you can use the following command:

```
cargo run -- add --db-path ledger.db --encryption-key "my-secret-key" --person "John Doe" --amount 100 --date "2023-01-01"
```

### Android

To build the Android application, you will need to have Android Studio installed. You can then open the `android` directory in Android Studio and build the application.

To run the application, you can use the Android Studio emulator or a physical device.

## Usage

### CLI

The CLI provides the following commands:

* `add`: Adds a new transaction.
* `list`: Lists all transactions.
* `balance`: Gets the balance for a specific person.
* `balances`: Lists all balances.
* `init-db`: Initializes a new database.
* `open-db`: Opens an existing database.
* `backup-db`: Backs up the database.
* `restore-db`: Restores the database from a backup.

For more information on each command, you can use the `--help` flag.

### Android

The Android application provides a graphical user interface for managing the ledger. You can use the application to add new transactions, view your transaction history, and see your balances.
