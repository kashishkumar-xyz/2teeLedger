//! # Ledger Library
//!
//! A library for managing a ledger database.
//!
//! This library provides a set of modules for interacting with the ledger,
//! including database management, backup and recovery, and key management.

pub mod backup;
pub mod db;
pub mod ffi;
pub mod key_management;
pub mod models;
pub mod recovery;
