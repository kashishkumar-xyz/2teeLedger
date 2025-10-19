use hmac::{Hmac, Mac};
use sha2::Sha256;
use std::fs;

type HmacSha256 = Hmac<Sha256>;

/// Calculates the HMAC-SHA256 checksum of the given data.
///
/// # Arguments
///
/// * `data` - The data to calculate the checksum for.
/// * `key` - The key to use for the HMAC.
///
/// # Returns
///
/// The calculated checksum.
pub fn calculate_checksum(data: &[u8], key: &[u8]) -> Vec<u8> {
    let mut mac = HmacSha256::new_from_slice(key).expect("HMAC can take key of any size");
    mac.update(data);
    mac.finalize().into_bytes().to_vec()
}

/// Backs up the database by copying the database file to a new location.
///
/// # Arguments
///
/// * `db_path` - The path to the database file.
/// * `backup_path` - The path to the backup file.
///
/// # Returns
///
/// Returns `Ok(())` if the backup was successful, or an error if the backup failed.
pub fn backup_db(db_path: &str, backup_path: &str) -> std::io::Result<()> {
    fs::copy(db_path, backup_path)?;
    Ok(())
}
